package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuickCatalogEntryDto {
    private Integer catalogNumber;
    private String catName;
    private String gender;
    private String emsCode;
    private String breed;
    private Integer category;
    private String showClass;
    private String group;
}