package com.gpfteam.catshow.catshow_backend.repository;
import com.gpfteam.catshow.catshow_backend.model.Registration;
import com.gpfteam.catshow.catshow_backend.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByOwnerEmailOrderByCreatedAtDesc(String email);
    Optional<Registration> findByRegistrationNumber(String registrationNumber);
    List<Registration> findByShowIdAndOwnerEmail(Long showId, String email);
    List<Registration> findByShowIdAndStatus(Long showId, Registration.RegistrationStatus status);
    @Query("SELECT r FROM Registration r JOIN FETCH r.entries c " +
            "WHERE r.show.id = :showId AND r.status = 'CONFIRMED'")
    List<Registration> findConfirmedRegistrationsWithCatsByShowId(@Param("showId") Long showId);
    List<Registration> findByShowAndStatus(Show show, Registration.RegistrationStatus status);
    long countByStatus(Registration.RegistrationStatus status);
    List<Registration> findByShow(Show show);
    long countByShowId(Long showId);
    long countByShowIdAndStatus(Long showId, Registration.RegistrationStatus status);
}
