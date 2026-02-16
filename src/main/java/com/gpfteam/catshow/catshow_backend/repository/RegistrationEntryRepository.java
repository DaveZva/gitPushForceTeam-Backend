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

    @Query("SELECT re FROM RegistrationEntry re WHERE re.registration.show.id = :showId")
    List<RegistrationEntry> findByShowId(@Param("showId") Long showId);

    @Query("SELECT re FROM RegistrationEntry re JOIN re.registration r WHERE r.show.id = :showId AND r.status = 'CONFIRMED' AND (r.days LIKE %:day% OR r.days = 'BOTH')")
    List<RegistrationEntry> findByShowAndStatusConfirmed(Long showId, String day);
}
