package com.gpfteam.catshow.catshow_backend.repository;

import com.gpfteam.catshow.catshow_backend.model.JudgingSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JudgingSheetRepository extends JpaRepository<JudgingSheet, Long> {
    List<JudgingSheet> findByShowIdAndJudgeIdAndDay(Long showId, Long judgeId, String day);
    List<JudgingSheet> findByShowIdAndDay(Long showId, String day);

    @Modifying
    @Query("DELETE FROM JudgingSheet j WHERE j.show.id = :showId AND j.judge.id = :judgeId")
    void deleteByShowIdAndJudgeId(Long showId, Long judgeId);

    @Modifying
    @Query("DELETE FROM JudgingSheet j WHERE j.show.id = :showId AND j.day = :day")
    void deleteByShowIdAndDay(Long showId, String day);
}