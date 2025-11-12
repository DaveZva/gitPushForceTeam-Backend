package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.OwnerRegistrationResponse;
import com.gpfteam.catshow.catshow_backend.model.Registration;
import com.gpfteam.catshow.catshow_backend.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/my-registrations")
@RequiredArgsConstructor
public class RegistrationQueryController {

    private final RegistrationRepository registrationRepository;

    @GetMapping
    public ResponseEntity<List<OwnerRegistrationResponse>> getMyRegistrations() {
        // 1. Získáme email přihlášeného uživatele z tokenu
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).build(); // Neautorizovaný přístup
        }
        String userEmail = authentication.getName(); // Toto je email (viz User.java -> getUsername)

        List<Registration> registrations = registrationRepository.findByBreederEmailOrderByCreatedAtDesc(userEmail);

        List<OwnerRegistrationResponse> responseList = registrations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    private OwnerRegistrationResponse mapToDto(Registration reg) {
        return OwnerRegistrationResponse.builder()
                .id(reg.getId())
                .registrationNumber(reg.getRegistrationNumber())
                .exhibitionName(reg.getExhibition().getName())
                .submittedAt(reg.getCreatedAt())
                .status(reg.getStatus().name())
                .catCount(reg.getCats() != null ? reg.getCats().size() : 0)
                .build();
    }
}