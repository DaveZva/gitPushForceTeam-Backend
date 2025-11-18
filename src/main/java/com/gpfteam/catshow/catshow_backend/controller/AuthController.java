package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.AuthResponse;
import com.gpfteam.catshow.catshow_backend.dto.LoginRequest;
import com.gpfteam.catshow.catshow_backend.dto.RegisterRequest;
import com.gpfteam.catshow.catshow_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        authService.forgotPassword(email);
        return ResponseEntity.ok().body(Map.of("message", "Pokud email existuje, instrukce byly odeslány."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newPassword = payload.get("newPassword");

        boolean success = authService.resetPassword(token, newPassword);
        if (success) {
            return ResponseEntity.ok().body(Map.of("message", "Heslo bylo úspěšně změněno."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Neplatný nebo expirovaný token."));
        }
    }
}