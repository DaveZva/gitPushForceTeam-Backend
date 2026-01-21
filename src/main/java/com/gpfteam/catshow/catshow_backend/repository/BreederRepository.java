package com.gpfteam.catshow.catshow_backend.repository;
import com.gpfteam.catshow.catshow_backend.model.Breeder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface BreederRepository extends JpaRepository<Breeder, Long> {
    @Query("SELECT DISTINCT r.breeder FROM Registration r WHERE r.user.email = :email ORDER BY r.breeder.lastName ASC")
    List<Breeder> findPreviousBreedersByUser(@Param("email") String email);
}