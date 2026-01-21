package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.RegistrationPayload;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationResponse;
import com.gpfteam.catshow.catshow_backend.model.Breeder;
import com.gpfteam.catshow.catshow_backend.model.Owner;
import com.gpfteam.catshow.catshow_backend.service.RegistrationService;
import com.gpfteam.catshow.catshow_backend.dto.RegistrationDetailResponse;
import com.gpfteam.catshow.catshow_backend.repository.OwnerRepository;
import com.gpfteam.catshow.catshow_backend.repository.BreederRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final OwnerRepository ownerRepository;
    private final BreederRepository breederRepository;

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

    @GetMapping("/history/owners")
    public ResponseEntity<List<Owner>> getPreviousOwners(Authentication authentication) {
        String email = authentication.getName();
        List<Owner> owners = ownerRepository.findPreviousOwnersByUser(email);

        return ResponseEntity.ok(owners);
    }

    @GetMapping("/history/breeders")
    public ResponseEntity<List<Breeder>> getPreviousBreeders(Authentication authentication) {
        String email = authentication.getName();
        List<Breeder> breeders = breederRepository.findPreviousBreedersByUser(email);
        return ResponseEntity.ok(breeders);
    }
}