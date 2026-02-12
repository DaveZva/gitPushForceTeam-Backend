package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.model.Cat;
import com.gpfteam.catshow.catshow_backend.model.Registration;
import com.gpfteam.catshow.catshow_backend.model.RegistrationEntry;
import com.gpfteam.catshow.catshow_backend.model.Show;
import com.gpfteam.catshow.catshow_backend.repository.RegistrationEntryRepository;
import com.gpfteam.catshow.catshow_backend.repository.RegistrationRepository;
import com.gpfteam.catshow.catshow_backend.repository.ShowRepository;
import com.gpfteam.catshow.catshow_backend.util.EmsUtility;
import com.gpfteam.catshow.catshow_backend.dto.QuickCatalogEntryDto;
import com.gpfteam.catshow.catshow_backend.dto.PublicCatalogEntryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogService {

    private final ShowRepository showRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationEntryRepository registrationEntryRepository;

    @Transactional
    public int closeShowAndGenerateCatalog(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found"));

        log.info("Spouštím generování katalogu pro výstavu: {}", show.getName());

        if (show.getStatus() != Show.ShowStatus.CLOSED) {
            show.setStatus(Show.ShowStatus.CLOSED);
            showRepository.save(show);
        }

        List<Registration> confirmedRegistrations = registrationRepository.findByShowAndStatus(
                show,
                Registration.RegistrationStatus.CONFIRMED
        );

        List<RegistrationEntry> entries = confirmedRegistrations.stream()
                .flatMap(reg -> reg.getEntries().stream())
                .collect(Collectors.toList());

        entries.sort(Comparator
                .comparingInt((RegistrationEntry e) -> EmsUtility.getCategory(e.getCat().getEmsCode()))
                .thenComparing((RegistrationEntry e) -> e.getCat().getEmsCode().split(" ")[0])
                .thenComparing((RegistrationEntry e) -> e.getCat().getCatGroup(), Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparingInt(e -> getClassRank(e.getShowClass()))
                .thenComparing(e -> e.getCat().getGender())
                .thenComparing(e -> e.getCat().getCatName())
        );

        int counter = 1;
        for (RegistrationEntry entry : entries) {
            entry.setCatalogNumber(counter++);
        }

        registrationEntryRepository.saveAll(entries);
        log.info("Generování katalogu pro výstavu '{}' dokončeno. Celkem očíslováno {} koček.", show.getName(), entries.size());
        return entries.size();
    }

    @Transactional
    public void assignNumberToLateEntry(RegistrationEntry entry) {
        Long showId = entry.getRegistration().getShow().getId();
        Integer maxNum = registrationEntryRepository.findMaxCatalogNumberByShowId(showId);

        int nextNum = (maxNum == null) ? 1 : maxNum + 1;
        entry.setCatalogNumber(nextNum);
        registrationEntryRepository.save(entry);
    }

    private int getClassRank(RegistrationEntry.ShowClass showClass) {
        if (showClass == null) return 99;
        return switch (showClass) {
            case SUPREME_CHAMPION -> 1;
            case SUPREME_PREMIOR -> 2;
            case GRANT_INTER_CHAMPION -> 3;
            case GRANT_INTER_PREMIER -> 4;
            case INTERNATIONAL_CHAMPION -> 5;
            case INTERNATIONAL_PREMIER -> 6;
            case CHAMPION -> 7;
            case PREMIER -> 8;
            case OPEN -> 9;
            case NEUTER -> 10;
            case JUNIOR -> 11;
            case KITTEN -> 12;
            case NOVICE_CLASS -> 13;
            case CONTROL_CLASS -> 14;
            case LITTER -> 15;
            case VETERAN -> 16;
            case DOMESTIC_CAT -> 17;
            default -> 50;
        };
    }

    public List<QuickCatalogEntryDto> getQuickCatalog(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found"));

        List<Registration> confirmedRegistrations = registrationRepository.findByShowAndStatus(
                show, Registration.RegistrationStatus.CONFIRMED
        );

        List<RegistrationEntry> entries = confirmedRegistrations.stream()
                .flatMap(reg -> reg.getEntries().stream())
                .filter(e -> e.getCatalogNumber() != null)
                .collect(Collectors.toList());

        entries.sort(Comparator
                .comparingInt((RegistrationEntry e) -> EmsUtility.getCategory(e.getCat().getEmsCode()))
                .thenComparing((RegistrationEntry e) -> e.getCat().getEmsCode().split(" ")[0])
                .thenComparing((RegistrationEntry e) -> e.getCat().getCatGroup(), Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparingInt(e -> getClassRank(e.getShowClass()))
                .thenComparing(e -> e.getCat().getCatName())
        );

        return entries.stream().map(e -> {
            Cat c = e.getCat();

            String fullName = (c.getTitleBefore() != null ? c.getTitleBefore() + " " : "")
                    + c.getCatName()
                    + (c.getTitleAfter() != null ? " " + c.getTitleAfter() : "");

            return QuickCatalogEntryDto.builder()
                    .catalogNumber(e.getCatalogNumber())
                    .catName(fullName.trim())
                    .gender(c.getGender().name())
                    .emsCode(c.getEmsCode())
                    .group(c.getCatGroup())
                    .showClass(e.getShowClass() != null ? e.getShowClass().name() : "")
                    .category(EmsUtility.getCategory(c.getEmsCode()))
                    .build();
        }).collect(Collectors.toList());
    }

    public List<PublicCatalogEntryDto> getCatalog(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found"));

        List<Registration> confirmedRegistrations = registrationRepository.findByShowAndStatus(
                show, Registration.RegistrationStatus.CONFIRMED
        );

        List<RegistrationEntry> entries = confirmedRegistrations.stream()
                .flatMap(reg -> reg.getEntries().stream())
                .filter(e -> e.getCatalogNumber() != null)
                .collect(Collectors.toList());

        entries.sort(Comparator
                .comparingInt((RegistrationEntry e) -> EmsUtility.getCategory(e.getCat().getEmsCode()))
                .thenComparing((RegistrationEntry e) -> e.getCat().getEmsCode().split(" ")[0])
                .thenComparing((RegistrationEntry e) -> e.getCat().getCatGroup(), Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparingInt(e -> getClassRank(e.getShowClass()))
                .thenComparing(e -> e.getCat().getGender())
                .thenComparing(e -> e.getCat().getCatName())
        );

        return entries.stream().map(this::mapToPublicDto).collect(Collectors.toList());
    }

    private PublicCatalogEntryDto mapToPublicDto(RegistrationEntry entry) {
        Cat c = entry.getCat();
        Registration reg = entry.getRegistration();

        String fullName = (c.getTitleBefore() != null ? c.getTitleBefore() + " " : "")
                + c.getCatName()
                + (c.getTitleAfter() != null ? " " + c.getTitleAfter() : "");

        return PublicCatalogEntryDto.builder()
                .id(entry.getId())
                .entryNumber(entry.getCatalogNumber())
                .name(fullName.trim())
                .ems(c.getEmsCode())
                .sex(c.getGender() == Cat.Gender.MALE ? "1,0" : "0,1")
                .birthDate(c.getBirthDate())
                .registrationNumber(c.getPedigreeNumber())
                .father(c.getFatherName())
                .mother(c.getMotherName())
                .ownerName(reg.getOwner().getFirstName() + " " + reg.getOwner().getLastName())
                .breederName(reg.getBreeder().getFirstName() + " " + reg.getBreeder().getLastName())
                .breederCountry(reg.getBreeder().getCity())
                .category(c.getEmsCode().split(" ")[0])
                .color(c.getEmsCode())
                .className(entry.getShowClass().name())
                .group(c.getCatGroup())
                .build();
    }
}