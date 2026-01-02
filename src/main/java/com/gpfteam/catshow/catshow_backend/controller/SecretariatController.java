package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.*;
import com.gpfteam.catshow.catshow_backend.model.*;
import com.gpfteam.catshow.catshow_backend.repository.*;
import com.gpfteam.catshow.catshow_backend.service.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/secretariat/shows")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SecretariatController {
    private final CatalogService catalogService;
    private final ShowRepository showRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final JudgeRepository judgeRepository;
    private final RegistrationEntryRepository registrationEntryRepository;

    @GetMapping
    public ResponseEntity<List<SecretariatShowDetailDto>> getAllShowsForSecretariat() {
        List<Show> shows = showRepository.findAll(Sort.by(Sort.Direction.DESC, "startDate"));

        List<SecretariatShowDetailDto> dtos = shows.stream().map(s -> SecretariatShowDetailDto.builder()
                .id(s.getId())
                .name(s.getName())
                .status(s.getStatus().name())
                .venueCity(s.getVenueCity())
                .venueName(s.getVenueName())
                .startDate(s.getStartDate().toLocalDate())
                .endDate(s.getEndDate().toLocalDate())
                .registrationDeadline(s.getRegistrationDeadline().toLocalDate())
                .maxCats(s.getMaxCats())
                .totalRegistrations(s.getRegistrations() != null ? s.getRegistrations().size() : 0) // Bezpečný výpočet
                .build()).toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/stats")
    public ResponseEntity<SecretariatStatsDto> getGlobalStats() {
        long totalCats = registrationEntryRepository.count();
        return ResponseEntity.ok(SecretariatStatsDto.builder()
                .totalCats(totalCats)
                .confirmedCats(totalCats)
                .totalUsers(userRepository.count())
                .totalRevenue(BigDecimal.ZERO)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecretariatShowDetailDto> getShowById(@PathVariable Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Výstava nenalezena"));

        List<Registration> registrations = registrationRepository.findByShow(show);
        int totalCats = 0;
        int confirmedRegs = 0;

        for (Registration reg : registrations) {
            if (reg.getEntries() != null) totalCats += reg.getEntries().size();
            if (reg.getStatus() == Registration.RegistrationStatus.CONFIRMED) confirmedRegs++;
        }

        return ResponseEntity.ok(SecretariatShowDetailDto.builder()
                .id(show.getId())
                .name(show.getName())
                .status(show.getStatus().name())
                .venueCity(show.getVenueCity())
                .startDate(show.getStartDate().toLocalDate())
                .endDate(show.getEndDate().toLocalDate())
                .registrationDeadline(show.getRegistrationDeadline().toLocalDate())
                .totalRegistrations(registrations.size())
                .totalCats(totalCats)
                .confirmedRegistrations(confirmedRegs)
                .maxCats(show.getMaxCats())
                .judgesCount(show.getJudges() != null ? show.getJudges().size() : 0)
                .build());
    }

    @GetMapping("/{showId}/registrations")
    public ResponseEntity<List<SecretariatRegistrationDto>> getRegistrationsByShow(@PathVariable Long showId) {
        Show show = showRepository.findById(showId).orElseThrow();
        List<Registration> registrations = registrationRepository.findByShow(show);
        List<SecretariatRegistrationDto> dtos = new ArrayList<>();
        for (Registration reg : registrations) {
            for (var entry : reg.getEntries()) {
                dtos.add(SecretariatRegistrationDto.builder()
                        .id(entry.getId())
                        .registrationNumber(reg.getRegistrationNumber())
                        .catName(entry.getCat().getCatName())
                        .emsCode(entry.getCat().getEmsCode())
                        .gender(entry.getCat().getGender().name())
                        .showClass(entry.getShowClass() != null ? entry.getShowClass().name() : "")
                        .ownerName(reg.getOwner().getFirstName() + " " + reg.getOwner().getLastName())
                        .ownerEmail(reg.getOwner().getEmail())
                        .status(reg.getStatus().name())
                        .build());
            }
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/entries/{id}")
    public ResponseEntity<SecretariatEntryDetailDto> getEntryDetail(@PathVariable Long id) {
        RegistrationEntry entry = registrationEntryRepository.findById(id).orElseThrow();
        Cat cat = entry.getCat();
        return ResponseEntity.ok(SecretariatEntryDetailDto.builder()
                .entryId(entry.getId())
                .catId(cat.getId())
                .catName(cat.getCatName())
                .emsCode(cat.getEmsCode())
                .gender(cat.getGender().name())
                .birthDate(cat.getBirthDate())
                .pedigreeNumber(cat.getPedigreeNumber())
                .chipNumber(cat.getChipNumber())
                .showClass(entry.getShowClass() != null ? entry.getShowClass().name() : "")
                .catalogNumber(entry.getCatalogNumber() != null ? entry.getCatalogNumber().toString() : "")
                .build());
    }

    @PutMapping("/entries/{id}")
    public ResponseEntity<Void> updateEntry(@PathVariable Long id, @RequestBody SecretariatEntryDetailDto dto) {
        RegistrationEntry entry = registrationEntryRepository.findById(id).orElseThrow();
        Cat cat = entry.getCat();
        cat.setCatName(dto.getCatName());
        cat.setEmsCode(dto.getEmsCode());
        cat.setPedigreeNumber(dto.getPedigreeNumber());
        cat.setChipNumber(dto.getChipNumber());
        if(dto.getGender() != null) try { cat.setGender(Cat.Gender.valueOf(dto.getGender())); } catch (Exception e) {}
        if (dto.getShowClass() != null && !dto.getShowClass().isEmpty()) try { entry.setShowClass(RegistrationEntry.ShowClass.valueOf(dto.getShowClass())); } catch (Exception e) {}
        if (dto.getCatalogNumber() != null && !dto.getCatalogNumber().isEmpty()) try { entry.setCatalogNumber(Integer.parseInt(dto.getCatalogNumber())); } catch (Exception e) {}
        registrationEntryRepository.save(entry);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/judges")
    public ResponseEntity<List<Judge>> getAllJudges() {
        return ResponseEntity.ok(judgeRepository.findAll());
    }

    @PostMapping("/{showId}/judges")
    public ResponseEntity<Void> assignJudgeToShow(@PathVariable Long showId, @RequestBody Map<String, Long> payload) {
        Show show = showRepository.findById(showId).orElseThrow();
        show.getJudges().add(judgeRepository.findById(payload.get("judgeId")).orElseThrow());
        showRepository.save(show);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{showId}/judges/{judgeId}")
    public ResponseEntity<Void> removeJudgeFromShow(@PathVariable Long showId, @PathVariable Long judgeId) {
        Show show = showRepository.findById(showId).orElseThrow();
        show.getJudges().remove(judgeRepository.findById(judgeId).orElseThrow());
        showRepository.save(show);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Show> createShow(@Valid @RequestBody CreateShowRequest request) {
        Show show = Show.builder().name(request.getName()).status(Show.ShowStatus.PLANNED)
                .startDate(request.getStartDate()).endDate(request.getEndDate())
                .registrationDeadline(request.getRegistrationDeadline())
                .venueName(request.getVenueName()).venueCity(request.getVenueCity())
                .organizerName(request.getOrganizerName()).maxCats(request.getMaxCats()).build();
        return ResponseEntity.created(URI.create("/api/v1/secretariat/shows/" + showRepository.save(show).getId())).body(show);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Show> updateShow(@PathVariable Long id, @RequestBody Show showDetails) {
        return showRepository.findById(id).map(existingShow -> {
            existingShow.setName(showDetails.getName());
            existingShow.setStatus(showDetails.getStatus());
            return ResponseEntity.ok(showRepository.save(existingShow));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id) {
        showRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/generate-catalog")
    public ResponseEntity<Map<String, Object>> generateCatalog(@PathVariable Long id) {
        int count = catalogService.closeShowAndGenerateCatalog(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Katalog vygenerován");
        response.put("totalCats", count);
        return ResponseEntity.ok(response);
    }
}