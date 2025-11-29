package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.RegistrationPayload;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationResponse;
import com.gpfteam.catshow.catshow_backend.service.RegistrationService;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<RegistrationResponse> submitRegistration(
            @RequestBody RegistrationPayload payload
    ) {
        try {
            RegistrationResponse response = registrationService.submitRegistration(payload);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationDetailResponse> getRegistrationDetail(@PathVariable Long id) {
        try {
            RegistrationDetailResponse response = registrationService.getRegistrationDetail(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}