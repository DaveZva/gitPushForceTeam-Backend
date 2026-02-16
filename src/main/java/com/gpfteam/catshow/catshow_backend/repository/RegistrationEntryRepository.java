package com.gpfteam.catshow.catshow_backend.repository;

import com.gpfteam.catshow.catshow_backend.model.Registration;
import com.gpfteam.catshow.catshow_backend.model.RegistrationEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegistrationEntryRepository extends JpaRepository<RegistrationEntry, Long> {
    @Query("SELECT MAX(e.catalogNumber) FROM RegistrationEntry e WHERE e.registration.show.id = :showId")
    Integer findMaxCatalogNumberByShowId(@Param("showId") Long showId);

    @Query("SELECT COUNT(e) FROM RegistrationEntry e " +
            "WHERE e.registration.show.id = :showId " +
            "AND e.registration.status IN :statuses")
    Long countEntriesByShowIdAndStatus(Long showId, List<Registration.RegistrationStatus> statuses);

    @Query("SELECT COUNT(e) FROM RegistrationEntry e WHERE e.registration.status = :status")
    long countByRegistrationStatus(@Param("status") Registration.RegistrationStatus status);

    @Query("SELECT COUNT(e) FROM RegistrationEntry e WHERE e.registration.show.id = :showId AND e.registration.status = :status")
    long countByShowIdAndRegistrationStatus(@Param("showId") Long showId, @Param("status") Registration.RegistrationStatus status);

    @Query("SELECT e FROM RegistrationEntry e WHERE e.registration.show.id = :showId " +
            "AND e.registration.status = 'CONFIRMED' " +
            "AND (e.registration.days LIKE %:day% OR e.registration.days = 'BOTH')")
    List<RegistrationEntry> findByShowAndStatusConfirmed(@Param("showId") Long showId, @Param("day") String day);
}
