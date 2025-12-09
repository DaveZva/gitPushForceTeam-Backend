package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.model.Registration;
import com.gpfteam.catshow.catshow_backend.model.RegistrationEntry;
import com.gpfteam.catshow.catshow_backend.model.Show;
import com.gpfteam.catshow.catshow_backend.repository.RegistrationEntryRepository;
import com.gpfteam.catshow.catshow_backend.repository.RegistrationRepository;
import com.gpfteam.catshow.catshow_backend.repository.ShowRepository;
import com.gpfteam.catshow.catshow_backend.util.EmsUtility;
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
    public void closeShowAndGenerateCatalog(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found"));

        log.info("Spouštím generování katalogu pro výstavu: {}", show.getName());

        // 1. Změna stavu na CLOSED (pokud ještě není)
        if (show.getStatus() != Show.ShowStatus.CLOSED) {
            show.setStatus(Show.ShowStatus.CLOSED);
            showRepository.save(show);
        }

        // 2. Načíst všechny CONFIRMED registrace
        List<Registration> confirmedRegistrations = registrationRepository.findByShowAndStatus(
                show,
                Registration.RegistrationStatus.CONFIRMED
        );

        // 3. Vytáhnout z nich jednotlivé kočky (Entries)
        List<RegistrationEntry> entries = confirmedRegistrations.stream()
                .flatMap(reg -> reg.getEntries().stream())
                .collect(Collectors.toList());

        // 4. Seřadit podle klíče: Kategorie -> Plemeno -> Třída -> Pohlaví -> Jméno
        entries.sort(Comparator
                .comparingInt((RegistrationEntry e) -> EmsUtility.getCategory(e.getCat().getEmsCode()))
                .thenComparing(e -> e.getCat().getEmsCode())
                .thenComparingInt(e -> getClassRank(e.getShowClass())) // Vlastní řazení tříd
                .thenComparing(e -> e.getCat().getGender())
                .thenComparing(e -> e.getCat().getCatName())
        );

        // 5. Očíslovat 1..N
        int counter = 1;
        for (RegistrationEntry entry : entries) {
            entry.setCatalogNumber(counter++);
        }

        registrationEntryRepository.saveAll(entries);
        log.info("Katalog vygenerován. Očíslováno {} koček.", entries.size());
    }

    /**
     * Helper pro pozdní registrace - přidělí číslo na konec řady.
     */
    @Transactional
    public void assignNumberToLateEntry(RegistrationEntry entry) {
        Long showId = entry.getRegistration().getShow().getId();
        Integer maxNum = registrationEntryRepository.findMaxCatalogNumberByShowId(showId);

        int nextNum = (maxNum == null) ? 1 : maxNum + 1;
        entry.setCatalogNumber(nextNum);
        registrationEntryRepository.save(entry);
    }

    // Pomocná metoda pro řazení tříd sestupně (od Supreme po koťata)
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
}