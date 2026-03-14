package com.gpfteam.catshow.catshow_backend.repository;

import com.gpfteam.catshow.catshow_backend.model.JudgingSheet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JudgingSheetRepository extends JpaRepository<JudgingSheet, Long> {
    @EntityGraph(attributePaths = {"catEntry", "catEntry.cat", "judge"})
    @Query("SELECT j FROM JudgingSheet j WHERE j.show.id = :showId AND j.judge.id = :judgeId AND j.day = :day ORDER BY j.catalogNumber ASC")
    List<JudgingSheet> findByShowIdAndJudgeIdAndDay(@Param("showId") Long showId, @Param("judgeId") Long judgeId, @Param("day") String day);


    @EntityGraph(attributePaths = {"catEntry", "catEntry.cat", "judge"})
    @Query("SELECT j FROM JudgingSheet j WHERE j.show.id = :showId AND j.day = :day ORDER BY j.catalogNumber ASC")
    List<JudgingSheet> findByShowIdAndDay(@Param("showId") Long showId, @Param("day") String day);


    @Modifying
    @Query("DELETE FROM JudgingSheet j WHERE j.show.id = :showId AND j.judge.id = :judgeId")
    void deleteByShowIdAndJudgeId(Long showId, Long judgeId);

    @Modifying
    @Query("DELETE FROM JudgingSheet j WHERE j.show.id = :showId AND j.day = :day")
    void deleteByShowIdAndDay(Long showId, String day);
}