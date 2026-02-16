package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PublicShowDetailDto {
    private Long id;
    private String name;
    private String description;

    private String venueName;
    private String venueCity;
    private String organizerName;
    private String organizerWebsiteUrl;
    private String organizerContactEmail;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime vetCheckStart;
    private LocalDateTime judgingStart;
    private LocalDateTime judgingEnd;

    private Integer maxCats;
    private Long occupiedSpots;
}