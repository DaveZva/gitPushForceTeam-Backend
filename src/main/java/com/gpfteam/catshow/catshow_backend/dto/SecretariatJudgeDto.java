package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class SecretariatJudgeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private List<String> validGroups;
}