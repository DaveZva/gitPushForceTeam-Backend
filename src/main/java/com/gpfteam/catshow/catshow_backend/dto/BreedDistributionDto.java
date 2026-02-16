package com.gpfteam.catshow.catshow_backend.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreedDistributionDto {
    private String code;
    private String name;
    private Integer count;
}