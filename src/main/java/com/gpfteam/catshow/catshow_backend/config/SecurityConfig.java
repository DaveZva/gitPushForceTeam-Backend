package com.gpfteam.catshow.catshow_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http)) // Ponecháme CORS
                .csrf(csrf -> csrf.disable()) // Vypneme CSRF pro API

                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/exhibitions", "/api/v1/exhibitions/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/exhibitions/all").authenticated()
                        .requestMatchers("/api/v1/exhibitions", "/api/v1/exhibitions/**").authenticated()

                        // Všechny ostatní požadavky musí být ověřené
                        .anyRequest().authenticated()
                )

                // Řekneme Springu, aby nepoužíval session, budeme stateless (JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authenticationProvider(authenticationProvider)

                // Přidáme náš JWT filtr, aby běžel PŘED standardním filtrem
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}