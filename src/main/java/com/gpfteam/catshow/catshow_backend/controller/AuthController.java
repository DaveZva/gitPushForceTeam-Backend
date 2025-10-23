package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.AuthResponse;
import com.gpfteam.catshow.catshow_backend.dto.LoginRequest;
import com.gpfteam.catshow.catshow_backend.dto.RegisterRequest;
import com.gpfteam.catshow.catshow_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Cesta pro přihlášení a registraci
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Povolíme CORS
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
}