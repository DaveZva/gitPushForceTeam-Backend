package com.gpfteam.catshow.catshow_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateShowRequest {

    @NotBlank(message = "Název výstavy je povinný")
    private String name;

    private String description;

    @NotNull(message = "Datum začátku je povinné")
    private LocalDateTime startDate;

    @NotNull(message = "Datum konce je povinné")
    private LocalDateTime endDate;

    @NotNull(message = "Uzávěrka registrací je povinná")
    private LocalDateTime registrationDeadline;

    @NotBlank(message = "Název místa je povinný")
    private String venueName;

    private String venueAddress;

    @NotBlank(message = "Město konání je povinné")
    private String venueCity;

    private String venueState;
    private String venueZip;

    @NotBlank(message = "Název organizátora je povinný")
    private String organizerName;

    @Email(message = "Neplatný formát emailu")
    private String contactEmail;

    private String websiteUrl;

    @NotNull(message = "Maximální počet koček je povinný")
    @Min(value = 10, message = "Kapacita musí být alespoň 10")
    @Max(value = 2000, message = "Maximální kapacita je 2000")
    private Integer maxCats;

    private LocalDateTime vetCheckStart;
    private LocalDateTime judgingStart;
    private LocalDateTime judgingEnd;

    private String status;
}