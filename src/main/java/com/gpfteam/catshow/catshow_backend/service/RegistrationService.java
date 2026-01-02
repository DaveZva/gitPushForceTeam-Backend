package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.dto.CatPayload;
import com.gpfteam.catshow.catshow_backend.dto.PersonPayload;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationDetailResponse;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationPayload;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationResponse;
import com.gpfteam.catshow.catshow_backend.model.*;
import com.gpfteam.catshow.catshow_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final ShowRepository showRepository;
    private final RegistrationRepository registrationRepository;
    private final BreederRepository breederRepository;
    private final OwnerRepository ownerRepository;
    private final CatRepository catRepository;
    private final UserRepository userRepository;
    private final RegistrationEntryRepository registrationEntryRepository;

    @Transactional
    public RegistrationResponse submitRegistration(RegistrationPayload payload) {
        // 1. Načtení výstavy
        Show show = showRepository.findById(Long.parseLong(payload.getShow().getId()))
                .orElseThrow(() -> new IllegalArgumentException("Výstava s ID " + payload.getShow().getId() + " nenalezena."));

        // 2. Zpracování majitele (pro registraci - kontakt)
        PersonPayload bData = payload.getOwner();
        Owner owner = ownerRepository.findByEmail(bData.getEmail())
                .orElseGet(() -> {
                    Owner newOwner = Owner.builder()
                            .firstName(bData.getFirstName())
                            .lastName(bData.getLastName())
                            .address(bData.getAddress())
                            .zip(bData.getZip())
                            .city(bData.getCity())
                            .email(bData.getEmail())
                            .phone(bData.getPhone())
                            .build();
                    return ownerRepository.save(newOwner);
                });

        // 3. Zpracování chovatele
        Breeder breeder;
        if (payload.getBreeder() != null) {
            PersonPayload eData = payload.getBreeder();
            breeder = Breeder.builder()
                    .firstName(eData.getFirstName())
                    .lastName(eData.getLastName())
                    .address(eData.getAddress())
                    .zip(eData.getZip())
                    .city(eData.getCity())
                    .email(eData.getEmail())
                    .phone(eData.getPhone())
                    .build();
        } else {
            // Pokud není chovatel vyplněn, použijeme údaje majitele (běžná praxe, pokud si chovají sami)
            breeder = Breeder.builder()
                    .firstName(bData.getFirstName())
                    .lastName(bData.getLastName())
                    .address(bData.getAddress())
                    .zip(bData.getZip())
                    .city(bData.getCity())
                    .email(bData.getEmail())
                    .phone(bData.getPhone())
                    .build();
        }
        breederRepository.save(breeder);

        // 4. Vytvoření hlavičky registrace
        Registration registration = Registration.builder()
                .show(show)
                .days(payload.getShow().getDays())
                .owner(owner)
                .breeder(breeder)
                .notes(payload.getNotes())
                .dataAccuracy(payload.getConsents().getOrDefault("dataAccuracy", false))
                .gdprConsent(payload.getConsents().getOrDefault("gdpr", false))
                .status(Registration.RegistrationStatus.PLANNED)
                .build();

        // 5. Získání aktuálního uživatele (pro přiřazení koček k profilu)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
        }

        // 6. Zpracování koček (Profil + Entry)
        List<RegistrationEntry> entries = new ArrayList<>();

        for (CatPayload cData : payload.getCats()) {
            // A) Najdi nebo vytvoř profil kočky (Cat)
            Cat cat = findOrCreateCat(cData, currentUser);

            // B) Vytvoř položku přihlášky (RegistrationEntry)
            RegistrationEntry entry = mapToEntry(cData, registration, cat);
            entries.add(entry);
        }

        registration.setEntries(entries);

        // Dočasné ID pro uložení
        registration.setRegistrationNumber("PLANNED-" + System.currentTimeMillis());
        Registration savedRegistration = registrationRepository.save(registration);

        // Finální vygenerování čísla přihlášky
        String regNumber = "REG-" + Year.now().getValue() + "-" + savedRegistration.getId();
        savedRegistration.setRegistrationNumber(regNumber);
        registrationRepository.save(savedRegistration);

        return RegistrationResponse.builder()
                .registrationNumber(regNumber)
                .build();
    }

    /**
     * Pokusí se najít existující kočku pro uživatele (podle čipu nebo jména).
     * Pokud nenajde, vytvoří novou.
     */
    private Cat findOrCreateCat(CatPayload cData, User user) {
        Cat cat;

        if (user != null) {
            Cat probe = new Cat();
            probe.setOwnerUser(user);

            if (cData.getChipNumber() != null && !cData.getChipNumber().isEmpty()) {
                probe.setChipNumber(cData.getChipNumber());
            } else {
                probe.setCatName(cData.getCatName());
            }

            Optional<Cat> existing = catRepository.findOne(Example.of(probe));

            if (existing.isPresent()) {
                cat = existing.get();
                updateCatFields(cat, cData);
                return catRepository.save(cat);
            }
        }

        cat = new Cat();
        cat.setOwnerUser(user);
        updateCatFields(cat, cData);

        return catRepository.save(cat);
    }

    private RegistrationEntry mapToEntry(CatPayload cData, Registration reg, Cat cat) {
        RegistrationEntry entry = new RegistrationEntry();
        entry.setRegistration(reg);
        entry.setCat(cat);

        // ShowClass
        String showClassString = cData.getShowClass().toUpperCase().replace("-", "_");
        RegistrationEntry.ShowClass showClassEnum = RegistrationEntry.ShowClass.valueOf(showClassString);
        entry.setShowClass(showClassEnum);

        // CageType
        RegistrationEntry.CageType cageTypeEnum = RegistrationEntry.CageType.valueOf(cData.getCageType().toUpperCase());
        entry.setCageType(cageTypeEnum);

        // Neutered
        boolean isNeutered = "YES".equalsIgnoreCase(cData.getNeutered()) || "TRUE".equalsIgnoreCase(cData.getNeutered());
        entry.setNeutered(isNeutered);

        if (reg.getShow().getStatus() == Show.ShowStatus.CLOSED) {
            Integer maxNum = registrationEntryRepository.findMaxCatalogNumberByShowId(reg.getShow().getId());
            int nextNum = (maxNum == null) ? 1 : maxNum + 1;
            entry.setCatalogNumber(nextNum);
        }

        return entry;
    }

    public RegistrationDetailResponse getRegistrationDetail(Long id) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registrace nenalezena"));

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!registration.getOwner().getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new RuntimeException("Nemáte oprávnění zobrazit tuto registraci.");
        }

        return RegistrationDetailResponse.builder()
                .id(registration.getId())
                .registrationNumber(registration.getRegistrationNumber())
                .status(registration.getStatus())
                .amountPaid(registration.getAmountPaid())
                .paidAt(registration.getPaidAt())
                .showName(registration.getShow().getName())
                .build();
    }

    private void updateCatFields(Cat cat, CatPayload cData) {
        cat.setCatName(cData.getCatName());
        cat.setTitleBefore(cData.getTitleBefore());
        cat.setTitleAfter(cData.getTitleAfter());
        cat.setChipNumber(cData.getChipNumber());
        cat.setEmsCode(cData.getEmsCode());
        cat.setBirthDate(cData.getBirthDate());
        cat.setPedigreeNumber(cData.getPedigreeNumber());

        // Uložení skupiny (nezapomeňte přidat toto pole do entity Cat a CatPayload)
        cat.setCatGroup(cData.getGroup());

        // Bezpečný převod pohlaví
        if (cData.getGender() != null) {
            cat.setGender(Cat.Gender.valueOf(cData.getGender().toUpperCase()));
        }

        // Rodiče - Otec
        cat.setFatherTitleBefore(cData.getFatherTitleBefore());
        cat.setFatherName(cData.getFatherName());
        cat.setFatherTitleAfter(cData.getFatherTitleAfter());
        cat.setFatherEmsCode(cData.getFatherEmsCode());
        cat.setFatherBirthDate(cData.getFatherBirthDate());
        cat.setFatherChipNumber(cData.getFatherChipNumber());
        cat.setFatherPedigreeNumber(cData.getFatherPedigreeNumber());

        // Rodiče - Matka
        cat.setMotherTitleBefore(cData.getMotherTitleBefore());
        cat.setMotherName(cData.getMotherName());
        cat.setMotherTitleAfter(cData.getMotherTitleAfter());
        cat.setMotherEmsCode(cData.getMotherEmsCode());
        cat.setMotherBirthDate(cData.getMotherBirthDate());
        cat.setMotherChipNumber(cData.getMotherChipNumber());
        cat.setMotherPedigreeNumber(cData.getMotherPedigreeNumber());
    }
}