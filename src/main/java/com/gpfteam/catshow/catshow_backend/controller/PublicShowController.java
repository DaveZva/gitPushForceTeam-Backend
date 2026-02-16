package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.PublicCatalogEntryDto;
import com.gpfteam.catshow.catshow_backend.dto.PublicShowDetailDto;
import com.gpfteam.catshow.catshow_backend.dto.QuickCatalogEntryDto;
import com.gpfteam.catshow.catshow_backend.model.Cat;
import com.gpfteam.catshow.catshow_backend.model.Registration;
import com.gpfteam.catshow.catshow_backend.model.RegistrationEntry;
import com.gpfteam.catshow.catshow_backend.model.Show;
import com.gpfteam.catshow.catshow_backend.repository.RegistrationEntryRepository;
import com.gpfteam.catshow.catshow_backend.repository.ShowRepository;
import com.gpfteam.catshow.catshow_backend.repository.RegistrationRepository;
import java.util.ArrayList;

import com.gpfteam.catshow.catshow_backend.service.CatalogService;
import com.gpfteam.catshow.catshow_backend.util.EmsUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicShowController {

    private final ShowRepository showRepository;
    private final RegistrationRepository registrationRepository;
    private final CatalogService catalogService;
    private final RegistrationEntryRepository registrationEntryRepository;

    /**
     * GET /api/v1/shows/available
     * Veřejný endpoint pro registrační formulář
     */
    @GetMapping("/available")
    public ResponseEntity<List<Show>> getAvailableShows() {
        List<Show> openShows = showRepository.findByStatusOrderByStartDateAsc(Show.ShowStatus.OPEN);
        return ResponseEntity.ok(openShows);
    }

    @GetMapping("/{showId}/catalog")
    public ResponseEntity<List<PublicCatalogEntryDto>> getShowCatalog(@PathVariable Long showId) {
        List<Registration> registrations = registrationRepository.findConfirmedRegistrationsWithCatsByShowId(showId);
        List<PublicCatalogEntryDto> catalogEntries = new ArrayList<>();

        AtomicInteger counter = new AtomicInteger(1);

        for (Registration reg : registrations) {
            for (RegistrationEntry entry : reg.getEntries()) {
                catalogEntries.add(mapEntryToDto(entry, reg));
            }
        }

        return ResponseEntity.ok(catalogEntries);
    }

    private PublicCatalogEntryDto mapEntryToDto(RegistrationEntry entry, Registration reg) {
        Cat cat = entry.getCat();
        int entryNumber = entry.getCatalogNumber() != null ? entry.getCatalogNumber() : 0;

        String sexDisplay = cat.getGender() == Cat.Gender.MALE ? "1,0" : "0,1";
        String emsCode = cat.getEmsCode();
        String category = (emsCode != null && !emsCode.isEmpty()) ? emsCode.split(" ")[0] : "UNK";

        String breederName = "Unknown";
        String breederCountry = "--";
        if (reg.getBreeder() != null) {
            breederName = reg.getBreeder().getFirstName() + " " + reg.getBreeder().getLastName();
            breederCountry = reg.getBreeder().getCity() != null ? reg.getBreeder().getCity() : "--";
        }

        String fatherFullName = (cat.getFatherTitleBefore() != null ? cat.getFatherTitleBefore() + " " : "") +
                (cat.getFatherName() != null ? cat.getFatherName() : "") +
                (cat.getFatherTitleAfter() != null ? " " + cat.getFatherTitleAfter() : "");

        String motherFullName = (cat.getMotherTitleBefore() != null ? cat.getMotherTitleBefore() + " " : "") +
                (cat.getMotherName() != null ? cat.getMotherName() : "") +
                (cat.getMotherTitleAfter() != null ? " " + cat.getMotherTitleAfter() : "");

        return PublicCatalogEntryDto.builder()
                .id(cat.getId())
                .entryNumber(entryNumber)
                .name((cat.getTitleBefore() != null ? cat.getTitleBefore() + " " : "") +
                        cat.getCatName() +
                        (cat.getTitleAfter() != null ? " " + cat.getTitleAfter() : ""))
                .ems(emsCode)
                .sex(sexDisplay)
                .birthDate(cat.getBirthDate())
                .registrationNumber(cat.getPedigreeNumber())

                .father(fatherFullName.trim())
                .mother(motherFullName.trim())

                .ownerName(reg.getOwner().getFirstName() + " " + reg.getOwner().getLastName())
                .breederName(breederName)
                .breederCountry(breederCountry)
                .breed(EmsUtility.getBreedFromEms(cat.getEmsCode()))
                .category(EmsUtility.getCategory(cat.getEmsCode()))
                .color(emsCode)
                .className(entry.getShowClass() != null ? entry.getShowClass().name() : "")
                .group(cat.getCatGroup())
                .build();
    }

    @GetMapping("/{showId}/quick-catalog")
    public ResponseEntity<List<QuickCatalogEntryDto>> getQuickCatalog(@PathVariable Long showId) {
        if (!showRepository.existsById(showId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(catalogService.getQuickCatalog(showId));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<PublicShowDetailDto> getShowDetails(@PathVariable Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Výstava nenalezena")); // Nebo 404

        Long currentCount = registrationEntryRepository.countEntriesByShowIdAndStatus(
                id,
                Arrays.asList(
                        Registration.RegistrationStatus.PLANNED,
                        Registration.RegistrationStatus.CONFIRMED
                )
        );

        PublicShowDetailDto dto = PublicShowDetailDto.builder()
                .id(show.getId())
                .name(show.getName())
                .description(show.getDescription())

                .venueName(show.getVenueName())
                .venueCity(show.getVenueCity())
                .organizerName(show.getOrganizerName())
                .organizerWebsiteUrl(show.getOrganizerWebsiteUrl())
                .organizerContactEmail(show.getOrganizerContactEmail())

                .startDate(show.getStartDate())
                .endDate(show.getEndDate())
                .vetCheckStart(show.getVetCheckStart())
                .judgingStart(show.getJudgingStart())
                .judgingEnd(show.getJudgingEnd())

                .maxCats(show.getMaxCats())
                .occupiedSpots(currentCount)
                .build();

        return ResponseEntity.ok(dto);
    }
}

