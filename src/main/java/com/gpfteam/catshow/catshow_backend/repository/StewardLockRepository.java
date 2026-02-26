package com.gpfteam.catshow.catshow_backend.repository;

import com.gpfteam.catshow.catshow_backend.model.StewardLock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StewardLockRepository extends JpaRepository<StewardLock, Long> {
    List<StewardLock> findByShowId(Long showId);
    Optional<StewardLock> findByShowIdAndJudgeId(Long showId, Long judgeId);
    void deleteByShowIdAndJudgeIdAndUserId(Long showId, Long judgeId, Long userId);
}