package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecretariatRegistrationDto {
    private Long id;
    private String registrationNumber;
    private String titleBefore;
    private String catName;
    private String titleAfter;
    private String emsCode;
    private String gender;
    private String showClass;
    private String ownerName;
    private String ownerEmail;
    private String status;
    private String catGroup;
}