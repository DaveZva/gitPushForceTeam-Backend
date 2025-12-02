package com.gpfteam.catshow.catshow_backend.dto;

import com.gpfteam.catshow.catshow_backend.model.Registration.RegistrationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RegistrationDetailResponse {
    private Long id;
    private String registrationNumber;
    private RegistrationStatus status;
    private Long amountPaid;
    private LocalDateTime paidAt;
    private String showName;
}