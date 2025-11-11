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

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final ExhibitionRepository exhibitionRepository;
    private final RegistrationRepository registrationRepository;
    private final BreederRepository breederRepository;
    private final ExhibitorRepository exhibitorRepository;

    @Transactional
    public RegistrationResponse submitRegistration(RegistrationPayload payload) {

        Exhibition exhibition = exhibitionRepository.findById(Long.parseLong(payload.getShow().getId()))
                .orElseThrow(() -> new IllegalArgumentException("Výstava s ID " + payload.getShow().getId() + " nenalezena."));

        PersonPayload bData = payload.getBreeder();

        Breeder breeder = breederRepository.findByEmail(bData.getEmail())
                .orElseGet(() -> {
                    Breeder newBreeder = Breeder.builder()
                            .firstName(bData.getFirstName())
                            .lastName(bData.getLastName())
                            .address(bData.getAddress())
                            .zip(bData.getZip())
                            .city(bData.getCity())
                            .email(bData.getEmail())
                            .phone(bData.getPhone())
                            .build();
                    return breederRepository.save(newBreeder);
                });

        Exhibitor exhibitor = null;
        if (payload.getExhibitor() != null) {
            PersonPayload eData = payload.getExhibitor();

            exhibitor = Exhibitor.builder()
                    .firstName(eData.getFirstName())
                    .lastName(eData.getLastName())
                    .address(eData.getAddress())
                    .zip(eData.getZip())
                    .city(eData.getCity())
                    .email(eData.getEmail())
                    .phone(eData.getPhone())
                    .build();
            exhibitorRepository.save(exhibitor);
        }

        Registration registration = Registration.builder()
                .exhibition(exhibition)
                .days(payload.getShow().getDays())
                .breeder(breeder)
                .exhibitor(exhibitor)
                .notes(payload.getNotes())
                .dataAccuracy(payload.getConsents().getOrDefault("dataAccuracy", false))
                .gdprConsent(payload.getConsents().getOrDefault("gdpr", false))
                .build();

        Registration finalRegistration = registration;
        List<Cat> cats = payload.getCats().stream()
                .map(catPayload -> mapCatPayloadToEntity(catPayload, finalRegistration))
                .collect(Collectors.toList());

        registration.setCats(cats);

        Registration savedRegistration = registrationRepository.save(registration);

        String regNumber = "REG-" + Year.now().getValue() + "-" + savedRegistration.getId();

        savedRegistration.setRegistrationNumber(regNumber);
        registrationRepository.save(savedRegistration);

        return RegistrationResponse.builder()
                .registrationNumber(regNumber)
                .build();
    }

    private Cat mapCatPayloadToEntity(CatPayload cData, Registration reg) {
        return Cat.builder()
                .registration(reg)

                .titleBefore(cData.getTitleBefore())
                .catName(cData.getCatName())
                .titleAfter(cData.getTitleAfter())
                .chipNumber(cData.getChipNumber())
                .gender(cData.getGender())
                .neutered(cData.getNeutered())
                .emsCode(cData.getEmsCode())
                .birthDate(cData.getBirthDate())
                .showClass(cData.getShowClass())
                .pedigreeNumber(cData.getPedigreeNumber())
                .cageType(cData.getCageType())

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
}