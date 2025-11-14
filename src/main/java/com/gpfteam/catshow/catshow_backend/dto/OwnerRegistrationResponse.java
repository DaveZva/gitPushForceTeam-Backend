package com.gpfteam.catshow.catshow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerRegistrationResponse {
    private Long id;
    private String registrationNumber;
    private String showName;
    private LocalDateTime submittedAt;
    private String status;
    private int catCount;
}