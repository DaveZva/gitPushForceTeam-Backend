package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.model.Show;
import com.gpfteam.catshow.catshow_backend.repository.ShowRepository;
import com.gpfteam.catshow.catshow_backend.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/secretariat/shows")
@RequiredArgsConstructor
public class SecretariatController {
    private final CatalogService catalogService;
    private final ShowRepository showRepository;

    /**
     * GET /api/v1/secretariat/show
     * Získá VŠECHNY výstavy pro sekretariát, seřazené
     */
    @GetMapping
    public ResponseEntity<List<Show>> getAllShowsForSecretariat() {
        List<Show> allShows = showRepository.findAll(
                Sort.by(Sort.Direction.DESC, "startDate")
        );
        return ResponseEntity.ok(allShows);
    }

    /**
     * GET /api/v1/secretariat/shows/{id}
     * Získá jednu výstavu podle ID (pro editaci)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Show> getShowsById(@PathVariable Long id) {
        return showRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/v1/secretariat/show
     * Vytvoří novou výstavu
     */
    @PostMapping
    public ResponseEntity<Show> createShow(@RequestBody Show show) {
        show.setId(null);
        if (show.getStatus() == null) {
            show.setStatus(Show.ShowStatus.PLANNED);
        }
        Show savedShow = showRepository.save(show);
        return ResponseEntity.status(201).body(savedShow);
    }

    /**
     * PUT /api/v1/secretariat/show/{id}
     * Aktualizuje existující výstavu
     */
    @PutMapping("/{id}")
    public ResponseEntity<Show> updateShow(@PathVariable Long id, @RequestBody Show showDetails) {
        return showRepository.findById(id)
                .map(existingShow -> {
                    existingShow.setName(showDetails.getName());
                    existingShow.setDescription(showDetails.getDescription());
                    existingShow.setStatus(showDetails.getStatus());
                    existingShow.setVenueName(showDetails.getVenueName());
                    existingShow.setVenueAddress(showDetails.getVenueAddress());
                    existingShow.setVenueCity(showDetails.getVenueCity());
                    existingShow.setVenueState(showDetails.getVenueState());
                    existingShow.setVenueZip(showDetails.getVenueZip());
                    existingShow.setStartDate(showDetails.getStartDate());
                    existingShow.setEndDate(showDetails.getEndDate());
                    existingShow.setRegistrationDeadline(showDetails.getRegistrationDeadline());
                    existingShow.setOrganizerName(showDetails.getOrganizerName());
                    existingShow.setContactEmail(showDetails.getContactEmail());
                    existingShow.setWebsiteUrl(showDetails.getWebsiteUrl());

                    Show updatedShow = showRepository.save(existingShow);
                    return ResponseEntity.ok(updatedShow);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/v1/secretariat/show/{id}
     * Smaže výstavu
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id) {
        if (!showRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        showRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Status 204
    }

    @PostMapping("/{id}/generate-catalog")
    public ResponseEntity<Map<String, Object>> generateCatalog(@PathVariable Long id) {
        if (!showRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        int count = catalogService.closeShowAndGenerateCatalog(id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Katalog byl úspěšně vygenerován a výstava uzavřena.");
        response.put("totalCats", count);

        return ResponseEntity.ok(response);
    }
}
