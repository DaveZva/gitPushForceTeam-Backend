package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CatResponseDto {
    private Long id;
    private String catName;
    private String titleBefore;
    private String titleAfter;
    private String emsCode;
    private String breed;
    private Integer category;
    private String pedigreeNumber;
    private String chipNumber;
    private String neutered;
    private String birthDate;
    private String gender;
    private String group;

    private String fatherName;
    private String fatherTitleBefore;
    private String fatherTitleAfter;
    private String fatherBirthDate;
    private String fatherEmsCode;
    private String fatherPedigreeNumber;

    private String motherName;
    private String motherTitleBefore;
    private String motherTitleAfter;
    private String motherBirthDate;
    private String motherEmsCode;
    private String motherPedigreeNumber;
}