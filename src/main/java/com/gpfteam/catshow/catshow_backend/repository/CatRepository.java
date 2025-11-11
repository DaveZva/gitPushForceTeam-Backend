package com.gpfteam.catshow.catshow_backend.repository;
import com.gpfteam.catshow.catshow_backend.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CatRepository extends JpaRepository<Cat, Long> { }