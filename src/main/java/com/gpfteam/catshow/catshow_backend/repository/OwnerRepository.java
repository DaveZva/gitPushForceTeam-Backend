package com.gpfteam.catshow.catshow_backend.repository;
import com.gpfteam.catshow.catshow_backend.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmail(String email);

    @Query("SELECT DISTINCT r.owner FROM Registration r WHERE r.user.email = :email ORDER BY r.owner.lastName ASC")
    List<Owner> findPreviousOwnersByUser(@Param("email") String email);
}