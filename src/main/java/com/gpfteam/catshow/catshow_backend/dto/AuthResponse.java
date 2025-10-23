package com.gpfteam.catshow.catshow_backend.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String firstName;
    private String lastName;
    private String email;
}