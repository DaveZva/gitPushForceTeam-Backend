package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.dto.CatPayload;
import com.gpfteam.catshow.catshow_backend.dto.PersonPayload;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationPayload;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationResponse;
import com.gpfteam.catshow.catshow_backend.model.*;
import com.gpfteam.catshow.catshow_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationDetailResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final ShowRepository showRepository;
    private final RegistrationRepository registrationRepository;
    private final BreederRepository breederRepository;
    private final OwnerRepository ownerRepository;


    @Transactional
    public RegistrationResponse submitRegistration(RegistrationPayload payload) {
        Show show = showRepository.findById(Long.parseLong(payload.getShow().getId()))
                .orElseThrow(() -> new IllegalArgumentException("Výstava s ID " + payload.getShow().getId() + " nenalezena."));

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

        Registration finalRegistration = registration;
        List<Cat> cats = payload.getCats().stream()
                .map(catPayload -> mapCatPayloadToEntity(catPayload, finalRegistration))
                .collect(Collectors.toList());

        registration.setCats(cats);
        registration.setRegistrationNumber("PLANNED-" + System.currentTimeMillis()); // Dočasná unikátní hodnota
        Registration savedRegistration = registrationRepository.save(registration);

        String regNumber = "REG-" + Year.now().getValue() + "-" + savedRegistration.getId();

        savedRegistration.setRegistrationNumber(regNumber);
        registrationRepository.save(savedRegistration); // Toto je druhý, finální save

        return RegistrationResponse.builder()
                .registrationNumber(regNumber)
                .build();
    }

    private Cat mapCatPayloadToEntity(CatPayload cData, Registration reg) {

        Cat.Gender genderEnum = Cat.Gender.valueOf(cData.getGender().toUpperCase());
        Cat.Neutered neuteredEnum = Cat.Neutered.valueOf(cData.getNeutered().toUpperCase());
        Cat.CageType cageTypeEnum = Cat.CageType.valueOf(cData.getCageType().toUpperCase());

        String showClassString = cData.getShowClass().toUpperCase().replace("-", "_");
        Cat.ShowClass showClassEnum = Cat.ShowClass.valueOf(showClassString);

        return Cat.builder()
                .registration(reg)

                .titleBefore(cData.getTitleBefore())
                .catName(cData.getCatName())
                .titleAfter(cData.getTitleAfter())
                .chipNumber(cData.getChipNumber())
                .gender(genderEnum)
                .neutered(neuteredEnum)
                .emsCode(cData.getEmsCode())
                .birthDate(cData.getBirthDate())
                .showClass(showClassEnum)
                .pedigreeNumber(cData.getPedigreeNumber())
                .cageType(cageTypeEnum)

                .motherTitleBefore(cData.getMotherTitleBefore())
                .motherName(cData.getMotherName())
                .motherTitleAfter(cData.getMotherTitleAfter())
                .motherBreed(cData.getMotherBreed())
                .motherEmsCode(cData.getMotherEmsCode())
                .motherColor(cData.getMotherColor())
                .motherPedigreeNumber(cData.getMotherPedigreeNumber())

                .fatherTitleBefore(cData.getFatherTitleBefore())
                .fatherName(cData.getFatherName())
                .fatherTitleAfter(cData.getFatherTitleAfter())
                .fatherBreed(cData.getFatherBreed())
                .fatherEmsCode(cData.getFatherEmsCode())
                .fatherColor(cData.getFatherColor())
                .fatherPedigreeNumber(cData.getFatherPedigreeNumber())
                .build();
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
}
