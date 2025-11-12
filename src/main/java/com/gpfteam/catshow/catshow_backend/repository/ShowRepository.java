package com.gpfteam.catshow.catshow_backend.repository;

import com.gpfteam.catshow.catshow_backend.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByStatus(Show.ShowStatus status);

    List<Show> findByStatusOrderByStartDateAsc(Show.ShowStatus status);


}