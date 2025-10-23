package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.dto.AuthResponse;
import com.gpfteam.catshow.catshow_backend.dto.LoginRequest;
import com.gpfteam.catshow.catshow_backend.dto.RegisterRequest;
import com.gpfteam.catshow.catshow_backend.model.User;
import com.gpfteam.catshow.catshow_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Zkontroluj, zda uživatel již neexistuje
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Uživatel s tímto emailem již existuje.");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // BEZPEČNÉ HESLO
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // Tímto se ověří email a heslo. Pokud selže, vyhodí to výjimku.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Pokud ověření projde, najdeme uživatele a vygenerujeme token
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Uživatel nenalezen."));

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}