package com.gpfteam.catshow.catshow_backend.repository;

import com.gpfteam.catshow.catshow_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Metoda pro nalezení uživatele podle emailu
    Optional<User> findByEmail(String email);
}