package com.gpfteam.catshow.catshow_backend.repository;

import com.gpfteam.catshow.catshow_backend.model.JudgeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JudgeAssignmentRepository extends JpaRepository<JudgeAssignment, Long> {
    List<JudgeAssignment> findByShowId(Long showId);
    List<JudgeAssignment> findByShowIdAndDay(Long showId, String day);
    Optional<JudgeAssignment> findByShowIdAndJudgeId(Long showId, Long judgeId);
}