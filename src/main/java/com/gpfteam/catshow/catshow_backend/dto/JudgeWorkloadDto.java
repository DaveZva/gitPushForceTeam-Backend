package com.gpfteam.catshow.catshow_backend.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeWorkloadDto {
    private Long judgeId;
    private String judgeName;
    private List<String> qualifications;
    private Integer catsCount;
    private List<BreedDistributionDto> breedDistribution;
}