package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class SecretariatStatsDto {
    private long totalCats;
    private long confirmedCats;
    private long totalUsers;
    private BigDecimal totalRevenue;
}