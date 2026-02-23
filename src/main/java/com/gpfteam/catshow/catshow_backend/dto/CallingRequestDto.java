package com.gpfteam.catshow.catshow_backend.dto;

import com.gpfteam.catshow.catshow_backend.model.enums.UrgencyLevel;
import lombok.Data;

@Data
public class CallingRequestDto {
    private Long showId;
    private String tableNo;
    private String judgeName;
    private Integer catNumber;
    private String category;
    private UrgencyLevel urgency;
}