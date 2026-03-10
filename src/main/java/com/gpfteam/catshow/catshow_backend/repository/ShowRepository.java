package com.gpfteam.catshow.catshow_backend.repository;

import com.gpfteam.catshow.catshow_backend.model.Show;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByStatus(Show.ShowStatus status);

    List<Show> findByStatusOrderByStartDateAsc(Show.ShowStatus status);

    @EntityGraph(attributePaths = {"judges"})
    @Query("SELECT s FROM Show s WHERE s.id = :id")
    Optional<Show> findByIdWithJudges(@Param("id") Long id);


}