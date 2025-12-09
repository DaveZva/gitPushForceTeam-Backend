package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.PublicCatalogEntryDto;
import com.gpfteam.catshow.catshow_backend.model.Cat;
import com.gpfteam.catshow.catshow_backend.model.Registration;
import com.gpfteam.catshow.catshow_backend.model.RegistrationEntry;
import com.gpfteam.catshow.catshow_backend.model.Show;
import com.gpfteam.catshow.catshow_backend.repository.ShowRepository;
import com.gpfteam.catshow.catshow_backend.repository.RegistrationRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/exhibitions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicShowController {

    private final ShowRepository exhibitionRepository;
    private final RegistrationRepository registrationRepository;

    /**
     * GET /api/v1/exhibitions/available
     * Veřejný endpoint pro registrační formulář
     */
    @GetMapping("/available")
    public ResponseEntity<List<Show>> getAvailableExhibitions() {
        List<Show> openExhibitions = exhibitionRepository.findByStatusOrderByStartDateAsc(Show.ShowStatus.OPEN);
        return ResponseEntity.ok(openExhibitions);
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
                .category(category)
                .color(emsCode)
                .className(entry.getShowClass() != null ? entry.getShowClass().name() : "")
                .group(null)
                .build();
    }
}

