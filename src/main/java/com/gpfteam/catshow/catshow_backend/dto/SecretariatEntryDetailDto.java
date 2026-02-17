package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecretariatEntryDetailDto {
    private Long entryId;
    private Long catId;

    private String titleBefore;
    private String catName;
    private String titleAfter;
    private String emsCode;
    private String breed;
    private Integer category;
    private String catGroup;
    private String gender;
    private String birthDate;
    private String pedigreeNumber;
    private String chipNumber;

    private String showClass;
    private String showClassCode;
    private Integer showClassSortOrder;
    private String catalogNumber;
    private boolean neutered;
}