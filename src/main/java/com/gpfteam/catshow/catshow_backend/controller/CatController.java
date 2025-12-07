package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.CatResponseDto;
import com.gpfteam.catshow.catshow_backend.model.Cat;
import com.gpfteam.catshow.catshow_backend.model.Registration;
import com.gpfteam.catshow.catshow_backend.repository.CatRepository;
import com.gpfteam.catshow.catshow_backend.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cats")
@RequiredArgsConstructor
public class CatController {

    private final CatRepository catRepository;
    private final RegistrationRepository registrationRepository;

    /**
     * Vrátí všechny kočky přihlášeného uživatele.
     * Může přijmout parametr showId pro zjištění, zda už je kočka registrována.
     */
    @GetMapping("/my")
    public ResponseEntity<List<CatResponseDto>> getMyCats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        List<Cat> myCats = catRepository.findByOwnerUserEmail(email);

        List<CatResponseDto> dtos = myCats.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/registered-in-show/{showId}")
    public ResponseEntity<Set<Long>> getRegisteredCatIds(@PathVariable Long showId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        List<Registration> regs = registrationRepository.findByShowIdAndOwnerEmail(showId, email);

        Set<Long> registeredCatIds = regs.stream()
                .flatMap(r -> r.getEntries().stream())
                .map(entry -> entry.getCat().getId())
                .collect(Collectors.toSet());

        return ResponseEntity.ok(registeredCatIds);
    }

    private CatResponseDto mapToDto(Cat cat) {
        return CatResponseDto.builder()
                .id(cat.getId())
                .catName(cat.getCatName())
                .emsCode(cat.getEmsCode())
                .pedigreeNumber(cat.getPedigreeNumber())
                .chipNumber(cat.getChipNumber())
                .birthDate(cat.getBirthDate())
                .gender(cat.getGender().name())
                .fatherName(cat.getFatherName())
                .fatherTitleBefore(cat.getFatherTitleBefore())
                .fatherTitleAfter(cat.getFatherTitleAfter())
                .fatherEmsCode(cat.getFatherEmsCode())
                .motherName(cat.getMotherName())
                .motherTitleBefore(cat.getMotherTitleBefore())
                .motherTitleAfter(cat.getMotherTitleAfter())
                .motherEmsCode(cat.getMotherEmsCode())
                .build();
    }
}