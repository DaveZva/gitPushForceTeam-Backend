package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Data;

@Data
public class CatPayload {
    private String titleBefore;
    private String catName;
    private String titleAfter;
    private String chipNumber;
    private String gender;
    private String neutered;
    private String emsCode;
    private String birthDate;
    private String showClass;
    private String pedigreeNumber;
    private String cageType;

    private String motherTitleBefore;
    private String motherName;
    private String motherTitleAfter;
    private String motherBreed;
    private String motherEmsCode;
    private String motherPedigreeNumber;
    private String motherBirthDate;
    private String motherChipNumber;

    private String fatherTitleBefore;
    private String fatherName;
    private String fatherTitleAfter;
    private String fatherBreed;
    private String fatherEmsCode;
    private String fatherPedigreeNumber;
    private String fatherBirthDate;
    private String fatherChipNumber;
}