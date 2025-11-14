package com.gpfteam.catshow.catshow_backend.repository;
import com.gpfteam.catshow.catshow_backend.model.Breeder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface BreederRepository extends JpaRepository<Breeder, Long> {
}