package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.model.Show;
import com.gpfteam.catshow.catshow_backend.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exhibitions") // Veřejná cesta
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicExhibitionController {

    private final ShowRepository exhibitionRepository;

    /**
     * GET /api/v1/exhibitions/available
     * Veřejný endpoint pro registrační formulář
     */
    @GetMapping("/available")
    public ResponseEntity<List<Show>> getAvailableExhibitions() {
        List<Show> openExhibitions = exhibitionRepository.findByStatusOrderByStartDateAsc(Show.ShowStatus.OPEN);
        return ResponseEntity.ok(openExhibitions);
    }
}
