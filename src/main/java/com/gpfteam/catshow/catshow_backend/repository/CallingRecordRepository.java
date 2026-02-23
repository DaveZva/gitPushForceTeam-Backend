package com.gpfteam.catshow.catshow_backend.repository;

import com.gpfteam.catshow.catshow_backend.model.CallingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CallingRecordRepository extends JpaRepository<CallingRecord, Long> {
    List<CallingRecord> findByShowId(Long showId);
}