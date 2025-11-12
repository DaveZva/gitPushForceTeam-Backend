package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.model.Show;
import com.gpfteam.catshow.catshow_backend.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/secretariat/shows")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SecretariatController {

    private final ShowRepository exhibitionRepository;

    /**
     * GET /api/v1/secretariat/exhibition
     * Získá VŠECHNY výstavy pro sekretariát, seřazené
     */
    @GetMapping
    public ResponseEntity<List<Show>> getAllExhibitionsForSecretariat() {
        List<Show> allExhibitions = exhibitionRepository.findAll(
                Sort.by(Sort.Direction.DESC, "startDate")
        );
        return ResponseEntity.ok(allExhibitions);
    }

    /**
     * GET /api/v1/secretariat/exhibition/{id}
     * Získá jednu výstavu podle ID (pro editaci)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Show> getExhibitionById(@PathVariable Long id) {
        return exhibitionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/v1/secretariat/exhibition
     * Vytvoří novou výstavu
     */
    @PostMapping
    public ResponseEntity<Show> createExhibition(@RequestBody Show exhibition) {
        exhibition.setId(null); // Zajistíme, že vytváříme novou
        if (exhibition.getStatus() == null) {
            exhibition.setStatus(Show.ShowStatus.PLANNED);
        }
        Show savedExhibition = exhibitionRepository.save(exhibition);
        return ResponseEntity.status(201).body(savedExhibition);
    }

    /**
     * PUT /api/v1/secretariat/exhibition/{id}
     * Aktualizuje existující výstavu
     */
    @PutMapping("/{id}")
    public ResponseEntity<Show> updateExhibition(@PathVariable Long id, @RequestBody Show exhibitionDetails) {
        return exhibitionRepository.findById(id)
                .map(existingExhibition -> {
                    // Zkopírujeme všechna pole
                    existingExhibition.setName(exhibitionDetails.getName());
                    existingExhibition.setDescription(exhibitionDetails.getDescription());
                    existingExhibition.setStatus(exhibitionDetails.getStatus());
                    existingExhibition.setVenueName(exhibitionDetails.getVenueName());
                    existingExhibition.setVenueAddress(exhibitionDetails.getVenueAddress());
                    existingExhibition.setVenueCity(exhibitionDetails.getVenueCity());
                    existingExhibition.setVenueState(exhibitionDetails.getVenueState());
                    existingExhibition.setVenueZip(exhibitionDetails.getVenueZip());
                    existingExhibition.setStartDate(exhibitionDetails.getStartDate());
                    existingExhibition.setEndDate(exhibitionDetails.getEndDate());
                    existingExhibition.setRegistrationDeadline(exhibitionDetails.getRegistrationDeadline());
                    existingExhibition.setOrganizerName(exhibitionDetails.getOrganizerName());
                    existingExhibition.setContactEmail(exhibitionDetails.getContactEmail());
                    existingExhibition.setWebsiteUrl(exhibitionDetails.getWebsiteUrl());

                    Show updatedExhibition = exhibitionRepository.save(existingExhibition);
                    return ResponseEntity.ok(updatedExhibition);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/v1/secretariat/exhibition/{id}
     * Smaže výstavu
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExhibition(@PathVariable Long id) {
        if (!exhibitionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        exhibitionRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Status 204
    }
}
