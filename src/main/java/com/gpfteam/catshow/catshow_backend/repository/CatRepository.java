package com.gpfteam.catshow.catshow_backend.repository;

import com.gpfteam.catshow.catshow_backend.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatRepository extends JpaRepository<Cat, Long> {
    List<Cat> findByOwnerUserEmail(String email);
    boolean existsByChipNumber(String chipNumber);
    boolean existsByPedigreeNumber(String pedigreeNumber);
}
