package com.gpfteam.catshow.catshow_backend.repository;
import com.gpfteam.catshow.catshow_backend.model.Registration;
import com.gpfteam.catshow.catshow_backend.model.Show;
import com.gpfteam.catshow.catshow_backend.model.User;
import com.gpfteam.catshow.catshow_backend.model.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByOwnerEmailOrderByCreatedAtDesc(String email);
    Optional<Registration> findByRegistrationNumber(String registrationNumber);
    List<Registration> findByShowIdAndOwnerEmail(Long showId, String email);
    List<Registration> findByShowIdAndStatus(Long showId, RegistrationStatus status);
    List<Registration> findByUser(User user);

    List<Registration> findConfirmedRegistrationsWithCatsByShowId(@Param("showId") Long showId);
    List<Registration> findByShowAndStatus(Show show, RegistrationStatus status);
    long countByStatus(RegistrationStatus status);
    List<Registration> findByShow(Show show);
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.show.id = :showId")
    long countByShowId(@Param("showId") Long showId);

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.show.id = :showId AND r.status = :status")
    long countByShowIdAndStatus(@Param("showId") Long showId, @Param("status") RegistrationStatus status);

    @EntityGraph(attributePaths = {"entries", "entries.cat", "owner"})
    @Query("SELECT r FROM Registration r WHERE r.show.id = :showId")
    List<Registration> findAllWithDetailsByShowId(@Param("showId") Long showId);
}
