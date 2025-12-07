package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicCatalogEntryDto {
    private Long id;
    private int entryNumber;
    private String name;
    private String ems;
    private String sex;
    private String birthDate;
    private String registrationNumber;
    private String father;
    private String mother;
    private String ownerName;
    private String ownerCountry;
    private String breederName;
    private String breederCountry;

    private String category;
    private String color;
    private String className;
    private Integer group;
}