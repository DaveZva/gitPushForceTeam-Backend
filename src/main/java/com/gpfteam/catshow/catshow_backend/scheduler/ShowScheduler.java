package com.gpfteam.catshow.catshow_backend.scheduler;

import com.gpfteam.catshow.catshow_backend.model.Show;
import com.gpfteam.catshow.catshow_backend.repository.ShowRepository;
import com.gpfteam.catshow.catshow_backend.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShowScheduler {

    private final ShowRepository showRepository;
    private final CatalogService catalogService;

    @Scheduled(cron = "0 46 * * * *")
    public void checkShowDeadlines() {
        log.info("Scheduler: Kontrola uzávěrek výstav...");

        List<Show> showsToClose = showRepository.findByStatus(Show.ShowStatus.OPEN).stream()
                .filter(show -> show.getRegistrationDeadline().isBefore(LocalDateTime.now()))
                .toList();

        if (showsToClose.isEmpty()) {
            log.info("Žádné výstavy k uzavření.");
            return;
        }

        for (Show show : showsToClose) {
            log.info("Uzavírám výstavu ID: {} - {}", show.getId(), show.getName());
            try {
                catalogService.closeShowAndGenerateCatalog(show.getId());
            } catch (Exception e) {
                log.error("Chyba při generování katalogu pro show ID " + show.getId(), e);
            }
        }
    }
}