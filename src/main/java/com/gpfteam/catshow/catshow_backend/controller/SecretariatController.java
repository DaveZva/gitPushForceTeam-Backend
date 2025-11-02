package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.model.Exhibition;
import com.gpfteam.catshow.catshow_backend.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/secretariat/exhibition")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SecretariatController {

    private final ExhibitionRepository exhibitionRepository;

    /**
     * GET /api/v1/secretariat/exhibition
     * Získá VŠECHNY výstavy pro sekretariát, seřazené
     */
    @GetMapping
    public ResponseEntity<List<Exhibition>> getAllExhibitionsForSecretariat() {
        List<Exhibition> allExhibitions = exhibitionRepository.findAll(
                Sort.by(Sort.Direction.DESC, "startDate")
        );
        return ResponseEntity.ok(allExhibitions);
    }

    /**
     * GET /api/v1/secretariat/exhibition/{id}
     * Získá jednu výstavu podle ID (pro editaci)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Exhibition> getExhibitionById(@PathVariable Long id) {
        return exhibitionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/v1/secretariat/exhibition
     * Vytvoří novou výstavu
     */
    @PostMapping
    public ResponseEntity<Exhibition> createExhibition(@RequestBody Exhibition exhibition) {
        exhibition.setId(null); // Zajistíme, že vytváříme novou
        if (exhibition.getStatus() == null) {
            exhibition.setStatus(Exhibition.ExhibitionStatus.PLANNED);
        }
        Exhibition savedExhibition = exhibitionRepository.save(exhibition);
        return ResponseEntity.status(201).body(savedExhibition);
    }

    /**
     * PUT /api/v1/secretariat/exhibition/{id}
     * Aktualizuje existující výstavu
     */
    @PutMapping("/{id}")
    public ResponseEntity<Exhibition> updateExhibition(@PathVariable Long id, @RequestBody Exhibition exhibitionDetails) {
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

                    Exhibition updatedExhibition = exhibitionRepository.save(existingExhibition);
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
