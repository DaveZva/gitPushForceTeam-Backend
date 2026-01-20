package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SecretariatShowDetailDto {
    private Long id;
    private String name;
    private String description;
    private String status;

    // Místo konání
    private String venueName;
    private String venueCity;
    private String venueAddress;
    private String venueState;
    private String venueZip;

    // Organizátor
    private String organizerName;
    private String organizerContactEmail;
    private String organizerWebsiteUrl;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime registrationDeadline;

    // Harmonogram
    private LocalDateTime vetCheckStart;
    private LocalDateTime judgingStart;
    private LocalDateTime judgingEnd;

    // Statistiky
    private int totalCats;
    private int confirmedRegistrations;
    private int totalRegistrations;
    private int maxCats;
    private int judgesCount;
}