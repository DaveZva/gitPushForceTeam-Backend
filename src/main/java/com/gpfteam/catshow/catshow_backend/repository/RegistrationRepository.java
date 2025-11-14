package com.gpfteam.catshow.catshow_backend.repository;
import com.gpfteam.catshow.catshow_backend.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByOwnerEmailOrderByCreatedAtDesc(String email);
    Optional<Registration> findByRegistrationNumber(String registrationNumber);
}
