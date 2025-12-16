package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.model.Show;
import com.gpfteam.catshow.catshow_backend.repository.ShowRepository;
import com.gpfteam.catshow.catshow_backend.service.CatalogService;
import com.gpfteam.catshow.catshow_backend.dto.CreateShowRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
                    existingShow.setOrganizerContactEmail(showDetails.getOrganizerContactEmail());
                    existingShow.setOrganizerWebsiteUrl(showDetails.getOrganizerWebsiteUrl());

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
            return ResponseEntity
                    .notFound().build();
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

    @PostMapping
    public ResponseEntity<?> createShow(@Valid @RequestBody CreateShowRequest request) {

        if (request.getEndDate().isBefore(request.getStartDate())) {
            return ResponseEntity.badRequest().body("Datum konce musí být později než datum začátku.");
        }

        if (request.getRegistrationDeadline().isAfter(request.getStartDate())) {
            return ResponseEntity.badRequest().body("Uzávěrka registrací musí být před začátkem výstavy.");
        }

        // 2. Mapování DTO -> Entity
        Show show = Show.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(Show.ShowStatus.PLANNED) // Výchozí stav
                // Termíny
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .registrationDeadline(request.getRegistrationDeadline())
                // Místo
                .venueName(request.getVenueName())
                .venueAddress(request.getVenueAddress())
                .venueCity(request.getVenueCity())
                .venueState(request.getVenueState())
                .venueZip(request.getVenueZip())
                // Organizátor
                .organizerName(request.getOrganizerName())
                .organizerContactEmail(request.getOrganizerContactEmail())
                .organizerWebsiteUrl(request.getOrganizerWebsiteUrl())
                // Kapacita
                .maxCats(request.getMaxCats())
                // Harmonogram
                .vetCheckStart(request.getVetCheckStart())
                .judgingStart(request.getJudgingStart())
                .judgingEnd(request.getJudgingEnd())
                .build();

        Show savedShow = showRepository.save(show);

        return ResponseEntity
                .created(URI.create("/api/v1/secretariat/shows/" + savedShow.getId()))
                .body(savedShow);
    }
}
