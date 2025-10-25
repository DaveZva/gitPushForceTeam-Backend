package com.gpfteam.catshow.catshow_backend.repository; // Uprav si balíček

import com.gpfteam.catshow.catshow_backend.model.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    // "Najdi všechny výstavy, které mají specifický status"
    List<Exhibition> findByStatus(Exhibition.ExhibitionStatus status);

    List<Exhibition> findByStatusOrderByStartDateAsc(Exhibition.ExhibitionStatus status);


}