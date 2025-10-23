package com.gpfteam.catshow.catshow_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user") // "user" je často rezervované slovo v SQL
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true) // Každý email musí být unikátní
    private String email;

    private String password; // Zde bude uložený HASH

    // Metody z UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Můžeme zjednodušit, pro teď nemáme role (např. ADMIN)
        return List.of();
    }

    @Override
    public String getUsername() {
        // Jako "username" budeme používat email
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Pro zjednodušení můžeme nechat vše na 'true'
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}