package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PublicBoardDto {
    private Long judgeId;
    private String judgeName;
    private String tableNo;
    private Boolean isPaused;
    private List<BoardCatDto> currentCats;
    private List<BoardCatDto> preparingCats;
    private List<BoardCatDto> waitingCats;

    @Data
    @Builder
    public static class BoardCatDto {
        private Integer catalogNumber;
        private String ems;
        private String type;
        private String urgency;
    }
}