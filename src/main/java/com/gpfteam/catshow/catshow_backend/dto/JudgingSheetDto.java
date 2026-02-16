package com.gpfteam.catshow.catshow_backend.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgingSheetDto {
    private Long id;
    private Long judgeId;
    private Long catEntryId;
    private String catName;
    private String emsCode;
    private String catGroup;
    private Integer catalogNumber;
    private String status;
    private String day;
}