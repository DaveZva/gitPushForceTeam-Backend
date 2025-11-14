package com.gpfteam.catshow.catshow_backend.repository;
import com.gpfteam.catshow.catshow_backend.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmail(String email);
}