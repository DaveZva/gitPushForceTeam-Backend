package com.gpfteam.catshow.catshow_backend.repository;

import com.gpfteam.catshow.catshow_backend.model.RegistrationEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegistrationEntryRepository extends JpaRepository<RegistrationEntry, Long> {
    @Query("SELECT MAX(e.catalogNumber) FROM RegistrationEntry e WHERE e.registration.show.id = :showId")
    Integer findMaxCatalogNumberByShowId(@Param("showId") Long showId);
}
