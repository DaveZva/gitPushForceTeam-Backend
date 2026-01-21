package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.dto.CatPayload;
import com.gpfteam.catshow.catshow_backend.dto.PersonPayload;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationDetailResponse;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationPayload;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationResponse;
import com.gpfteam.catshow.catshow_backend.model.*;
import com.gpfteam.catshow.catshow_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    private final ShowRepository showRepository;
    private final RegistrationRepository registrationRepository;
    private final BreederRepository breederRepository;
    private final OwnerRepository ownerRepository;
    private final CatRepository catRepository;
    private final UserRepository userRepository;

    @Transactional
    public RegistrationResponse submitRegistration(RegistrationPayload payload) {
        Long showId;
        try {
            showId = Long.parseLong(payload.getShow().getId());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Neplatné ID výstavy.");
        }

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Výstava nenalezena."));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            currentUser = userRepository.findByEmail(auth.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Uživatel nenalezen"));
        } else {
            throw new IllegalStateException("Pro registraci musíte být přihlášen.");
        }

        Owner owner = findOrCreateOwner(payload.getOwner());
        Breeder breeder = findOrCreateBreeder(payload.getBreeder(), owner);

        Registration registration = Registration.builder()
                .show(show)
                .owner(owner)
                .breeder(breeder)
                .status(Registration.RegistrationStatus.PLANNED)
                .days(payload.getShow().getDays())
                .notes(payload.getNotes())
                .dataAccuracy(payload.getConsents() != null && payload.getConsents().getOrDefault("dataAccuracy", false))
                .gdprConsent(payload.getConsents() != null && payload.getConsents().getOrDefault("gdpr", false))
                .registrationNumber("PENDING-" + System.currentTimeMillis())
                .user(currentUser)
                .build();

        List<RegistrationEntry> entries = new ArrayList<>();
        if (payload.getCats() != null) {
            for (CatPayload cData : payload.getCats()) {
                Cat cat = findOrCreateCat(cData, currentUser);
                RegistrationEntry entry = mapToEntry(cData, registration, cat);
                entries.add(entry);
            }
        }
        registration.setEntries(entries);

        Registration savedRegistration = registrationRepository.save(registration);

        String finalRegNumber = "REG-" + Year.now().getValue() + "-" + savedRegistration.getId();
        savedRegistration.setRegistrationNumber(finalRegNumber);
        registrationRepository.save(savedRegistration);

        return RegistrationResponse.builder()
                .registrationId(savedRegistration.getId())
                .registrationNumber(finalRegNumber)
                .message("Registrace úspěšně vytvořena")
                .build();
    }

    private Owner findOrCreateOwner(PersonPayload data) {
        Owner probe = new Owner();
        probe.setEmail(data.getEmail());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withIgnoreNullValues();

        return ownerRepository.findOne(Example.of(probe, matcher))
                .orElseGet(() -> {
                    Owner newOwner = new Owner();
                    newOwner.setFirstName(data.getFirstName());
                    newOwner.setLastName(data.getLastName());
                    newOwner.setAddress(data.getAddress());
                    newOwner.setZip(data.getZip());
                    newOwner.setCity(data.getCity());
                    newOwner.setEmail(data.getEmail());
                    newOwner.setPhone(data.getPhone());
                    newOwner.setOwnerLocalOrganization(data.getOwnerLocalOrganization());
                    newOwner.setOwnerMembershipNumber(data.getOwnerMembershipNumber());
                    return ownerRepository.save(newOwner);
                });
    }

    private Breeder findOrCreateBreeder(PersonPayload data, Owner owner) {
        if (data == null || data.getFirstName() == null || data.getFirstName().isEmpty()) {
            Breeder copy = new Breeder();
            copy.setFirstName(owner.getFirstName());
            copy.setLastName(owner.getLastName());
            copy.setAddress(owner.getAddress());
            copy.setZip(owner.getZip());
            copy.setCity(owner.getCity());
            copy.setEmail(owner.getEmail());
            copy.setPhone(owner.getPhone());
            return breederRepository.save(copy);
        }

        Breeder probe = new Breeder();
        probe.setEmail(data.getEmail());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withIgnoreNullValues();

        return breederRepository.findOne(Example.of(probe, matcher))
                .orElseGet(() -> {
                    Breeder newBreeder = new Breeder();
                    newBreeder.setFirstName(data.getFirstName());
                    newBreeder.setLastName(data.getLastName());
                    newBreeder.setAddress(data.getAddress());
                    newBreeder.setZip(data.getZip());
                    newBreeder.setCity(data.getCity());
                    newBreeder.setEmail(data.getEmail());
                    newBreeder.setPhone(data.getPhone());
                    return breederRepository.save(newBreeder);
                });
    }

    private Cat findOrCreateCat(CatPayload cData, User user) {
        if (cData.getChipNumber() != null && !cData.getChipNumber().isEmpty()) {
            Cat probe = new Cat();
            probe.setChipNumber(cData.getChipNumber());
            probe.setOwnerUser(user);

            Optional<Cat> existing = catRepository.findOne(Example.of(probe));
            if (existing.isPresent()) {
                Cat cat = existing.get();
                updateCatFields(cat, cData);
                return catRepository.save(cat);
            }
        }

        Cat cat = new Cat();
        cat.setOwnerUser(user);
        updateCatFields(cat, cData);
        return catRepository.save(cat);
    }

    private void updateCatFields(Cat cat, CatPayload cData) {
        cat.setCatName(cData.getCatName());
        cat.setTitleBefore(cData.getTitleBefore());
        cat.setTitleAfter(cData.getTitleAfter());
        cat.setEmsCode(cData.getEmsCode());
        cat.setBirthDate(cData.getBirthDate());
        cat.setPedigreeNumber(cData.getPedigreeNumber());
        cat.setChipNumber(cData.getChipNumber());
        cat.setCatGroup(cData.getGroup());

        if (cData.getGender() != null) {
            try {
                cat.setGender(Cat.Gender.valueOf(cData.getGender().toUpperCase()));
            } catch (Exception e) {
                logger.warn("Neznámé pohlaví: {}", cData.getGender());
            }
        }

        cat.setFatherName(cData.getFatherName());
        cat.setFatherTitleBefore(cData.getFatherTitleBefore());
        cat.setFatherTitleAfter(cData.getFatherTitleAfter());
        cat.setFatherEmsCode(cData.getFatherEmsCode());
        cat.setFatherPedigreeNumber(cData.getFatherPedigreeNumber());
        cat.setFatherBirthDate(cData.getFatherBirthDate());
        cat.setFatherChipNumber(cData.getFatherChipNumber());

        cat.setMotherName(cData.getMotherName());
        cat.setMotherTitleBefore(cData.getMotherTitleBefore());
        cat.setMotherTitleAfter(cData.getMotherTitleAfter());
        cat.setMotherEmsCode(cData.getMotherEmsCode());
        cat.setMotherPedigreeNumber(cData.getMotherPedigreeNumber());
        cat.setMotherBirthDate(cData.getMotherBirthDate());
        cat.setMotherChipNumber(cData.getMotherChipNumber());
    }

    private RegistrationEntry mapToEntry(CatPayload cData, Registration reg, Cat cat) {
        RegistrationEntry entry = new RegistrationEntry();
        entry.setRegistration(reg);
        entry.setCat(cat);

        if (cData.getShowClass() != null) {
            entry.setShowClass(mapShowClass(cData.getShowClass()));
        }

        if (cData.getCageType() != null) {
            entry.setCageType(mapCageType(cData.getCageType()));
        }

        boolean isNeutered = "YES".equalsIgnoreCase(cData.getNeutered()) || "TRUE".equalsIgnoreCase(cData.getNeutered());
        entry.setNeutered(isNeutered);

        return entry;
    }

    private RegistrationEntry.ShowClass mapShowClass(String input) {
        try {
            return RegistrationEntry.ShowClass.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            switch (input.trim()) {
                case "1": return RegistrationEntry.ShowClass.SUPREME_CHAMPION;
                case "2": return RegistrationEntry.ShowClass.SUPREME_PREMIOR;
                case "3": return RegistrationEntry.ShowClass.GRANT_INTER_CHAMPION;
                case "4": return RegistrationEntry.ShowClass.GRANT_INTER_PREMIER;
                case "5": return RegistrationEntry.ShowClass.INTERNATIONAL_CHAMPION;
                case "6": return RegistrationEntry.ShowClass.INTERNATIONAL_PREMIER;
                case "7": return RegistrationEntry.ShowClass.CHAMPION;
                case "8": return RegistrationEntry.ShowClass.PREMIER;
                case "9": return RegistrationEntry.ShowClass.OPEN;
                case "10": return RegistrationEntry.ShowClass.NEUTER;
                case "11": return RegistrationEntry.ShowClass.JUNIOR;
                case "12": return RegistrationEntry.ShowClass.KITTEN;
                default:
                    logger.warn("Nenalezeno mapování pro třídu: {}", input);
                    return null;
            }
        }
    }

    private RegistrationEntry.CageType mapCageType(String input) {
        String normalized = input.toUpperCase().trim();
        if (normalized.contains("OWN")) return RegistrationEntry.CageType.OWN_CAGE;
        if (normalized.contains("SINGLE")) return RegistrationEntry.CageType.RENT_SMALL;
        if (normalized.contains("DOUBLE")) return RegistrationEntry.CageType.RENT_LARGE;

        try {
            return RegistrationEntry.CageType.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            return RegistrationEntry.CageType.OWN_CAGE;
        }
    }

    public RegistrationDetailResponse getRegistrationDetail(Long id) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registrace nenalezena"));

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean isOwner = registration.getOwner().getEmail().equalsIgnoreCase(currentUserEmail);

        if (!isOwner) {
            throw new RuntimeException("Neoprávněný přístup.");
        }

        return RegistrationDetailResponse.builder()
                .id(registration.getId())
                .registrationNumber(registration.getRegistrationNumber())
                .status(registration.getStatus())
                .amountPaid(registration.getAmountPaid())
                .showName(registration.getShow().getName())
                .build();
    }
}