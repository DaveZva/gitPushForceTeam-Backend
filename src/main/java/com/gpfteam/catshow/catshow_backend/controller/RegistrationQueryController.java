package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.OwnerRegistrationResponse;
import com.gpfteam.catshow.catshow_backend.model.Registration;
import com.gpfteam.catshow.catshow_backend.repository.RegistrationRepository;
import com.gpfteam.catshow.catshow_backend.service.PdfGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/my-registrations")
@RequiredArgsConstructor
public class RegistrationQueryController {

    private final RegistrationRepository registrationRepository;
    private final PdfGenerationService pdfGenerationService;

    @GetMapping
    public ResponseEntity<List<OwnerRegistrationResponse>> getMyRegistrations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).build();
        }
        String userEmail = authentication.getName();

        List<Registration> registrations = registrationRepository.findByOwnerEmailOrderByCreatedAtDesc(userEmail);

        List<OwnerRegistrationResponse> responseList = registrations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    private OwnerRegistrationResponse mapToDto(Registration reg) {
        return OwnerRegistrationResponse.builder()
                .id(reg.getId())
                .registrationNumber(reg.getRegistrationNumber())
                .showName(reg.getShow().getName())
                .submittedAt(reg.getCreatedAt())
                .status(reg.getStatus().name())
                .catCount(reg.getCats() != null ? reg.getCats().size() : 0)
                .build();
    }

    @GetMapping("/pdf/{registrationNumber}")
    public ResponseEntity<byte[]> getRegistrationPdf(@PathVariable String registrationNumber) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Registration registration = registrationRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registrace nenalezena"));

        if (!registration.getOwner().getEmail().equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        try {
            byte[] pdfContents = pdfGenerationService.generateRegistrationPdf(registration);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "registrace-" + registration.getRegistrationNumber() + ".pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}