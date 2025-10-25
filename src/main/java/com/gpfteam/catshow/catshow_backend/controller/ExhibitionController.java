package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.model.Exhibition;
import com.gpfteam.catshow.catshow_backend.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exhibitions") // Cesta pro výstavy
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExhibitionController {

    // Připojíme si repository pro přístup k databázi
    private final ExhibitionRepository exhibitionRepository;

    @GetMapping
    public ResponseEntity<List<Exhibition>> getOpenExhibitions() {
        List<Exhibition> openExhibitions = exhibitionRepository.findByStatusOrderByStartDateAsc(Exhibition.ExhibitionStatus.OPEN);
        return ResponseEntity.ok(openExhibitions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exhibition> getExhibitionById(@PathVariable Long id) {
        return exhibitionRepository.findById(id)
                .map(ResponseEntity::ok) // Pokud se najde, vrátí 200 OK
                .orElse(ResponseEntity.notFound().build()); // Pokud ne, vrátí 404 Not Found
    }

    @GetMapping("/all")
    public ResponseEntity<List<Exhibition>> getAllExhibitionsForAdmin() {
        // Použijeme metodu findAll a seřadíme je
        List<Exhibition> allExhibitions = exhibitionRepository.findAll(
                org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.DESC, "startDate"
                )
        );
        return ResponseEntity.ok(allExhibitions);
    }

    @PostMapping
    public ResponseEntity<Exhibition> createExhibition(@RequestBody Exhibition exhibition) {
        // Nastavíme výchozí status, pokud není zadán
        if (exhibition.getStatus() == null) {
            exhibition.setStatus(Exhibition.ExhibitionStatus.PLANNED);
        }
        Exhibition savedExhibition = exhibitionRepository.save(exhibition);
        // Vrátíme status 201 Created
        return ResponseEntity.status(201).body(savedExhibition);
    }
}