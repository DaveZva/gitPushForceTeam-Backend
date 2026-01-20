package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SecretariatPaymentDto {
    private Long registrationId;
    private String registrationNumber;
    private String ownerName;
    private String ownerEmail;
    private Long amount;
    private String status;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private int catCount;
}