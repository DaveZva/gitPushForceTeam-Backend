package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StewardJudgeDto {
    private Long id;
    private String name;
    private Integer tableNumber;
    private boolean isLocked;
    private String lockedBySteward;
    private boolean isLockedByMe;
}