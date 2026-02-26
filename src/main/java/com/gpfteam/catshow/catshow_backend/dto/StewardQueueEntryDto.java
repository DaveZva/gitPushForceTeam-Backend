package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StewardQueueEntryDto {
    private Long id;
    private Integer catalogNumber;
    private String catName;
    private String ems;
    private String sex;
    private String birthDate;
    private String status;
    private String urgency;
    private Long callingRecordId;
    private String group;
    private String category;
    private String breed;
    private Long judgeId;
}