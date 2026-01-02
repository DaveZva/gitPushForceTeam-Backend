package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class SecretariatShowDetailDto {
    private Long id;
    private String name;
    private String status;
    private String venueCity;
    private String venueName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate registrationDeadline;

    private int totalCats;
    private int confirmedRegistrations;
    private int totalRegistrations;
    private int maxCats;
    private int judgesCount;
}