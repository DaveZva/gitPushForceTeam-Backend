package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.dto.StewardJudgeDto;
import com.gpfteam.catshow.catshow_backend.dto.StewardQueueEntryDto;
import com.gpfteam.catshow.catshow_backend.model.*;
import com.gpfteam.catshow.catshow_backend.model.enums.JudgingStatus;
import com.gpfteam.catshow.catshow_backend.model.enums.UrgencyLevel;
import com.gpfteam.catshow.catshow_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StewardService {

    private final JudgingSheetRepository judgingSheetRepository;
    private final StewardLockRepository stewardLockRepository;
    private final CallingRecordRepository callingRecordRepository;
    private final UserRepository userRepository;

    public List<StewardJudgeDto> getJudgesForShow(Long showId, String email) {
        User currentUser = userRepository.findByEmail(email).orElseThrow();
        List<JudgingSheet> sheets = judgingSheetRepository.findByShowIdAndDay(showId, "SATURDAY");
        List<Judge> judges = sheets.stream().map(JudgingSheet::getJudge).distinct().toList();
        List<StewardLock> locks = stewardLockRepository.findByShowId(showId);

        return judges.stream().map(judge -> {
            Optional<StewardLock> lockOpt = locks.stream().filter(l -> l.getJudgeId().equals(judge.getId())).findFirst();
            boolean isLocked = lockOpt.isPresent();
            boolean isLockedByMe = isLocked && lockOpt.get().getUserId().equals(currentUser.getId());

            return StewardJudgeDto.builder()
                    .id(judge.getId())
                    .name(judge.getFirstName() + " " + judge.getLastName())
                    .isLocked(isLocked)
                    .lockedBySteward(isLocked ? lockOpt.get().getStewardName() : null)
                    .isLockedByMe(isLockedByMe)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional
    public boolean lockJudge(Long showId, Long judgeId, String email, Integer tableNumber) {
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        Optional<StewardLock> existingLock = stewardLockRepository.findByShowIdAndJudgeId(showId, judgeId);
        if (existingLock.isPresent()) {
            return existingLock.get().getUserId().equals(currentUser.getId());
        }

        boolean tableTaken = stewardLockRepository.findByShowId(showId).stream()
                .anyMatch(l -> l.getTableNumber() != null && l.getTableNumber().equals(tableNumber));

        if (tableTaken) {
            return false;
        }

        StewardLock newLock = StewardLock.builder()
                .showId(showId)
                .judgeId(judgeId)
                .userId(currentUser.getId())
                .stewardName(currentUser.getFirstName() + " " + currentUser.getLastName())
                .tableNumber(tableNumber)
                .build();

        stewardLockRepository.save(newLock);
        return true;
    }

    @Transactional
    public void unlockJudge(Long showId, Long judgeId, String email) {
        User currentUser = userRepository.findByEmail(email).orElseThrow();
        stewardLockRepository.deleteByShowIdAndJudgeIdAndUserId(showId, judgeId, currentUser.getId());
    }

    public List<StewardQueueEntryDto> getQueue(Long showId, Long judgeId) {
        List<JudgingSheet> sheets = judgingSheetRepository.findByShowIdAndJudgeIdAndDay(showId, judgeId, "SATURDAY");
        List<CallingRecord> activeCalls = callingRecordRepository.findByShowId(showId);

        Map<Integer, CallingRecord> callMap = activeCalls.stream().collect(Collectors.toMap(CallingRecord::getCatNumber, c -> c, (a, b) -> a));

        return sheets.stream().map(sheet -> {CallingRecord activeCall = callMap.get(sheet.getCatalogNumber());
            boolean hasCall = activeCall != null;

            String status = sheet.getStatus().name();

            if (sheet.getStatus() == JudgingStatus.PENDING) {
                status = "WAITING";
            } else if (sheet.getStatus() == JudgingStatus.READY) {
                status = "READY";
            } else if (sheet.getStatus() == JudgingStatus.IN_PROGRESS) {
                status = "JUDGING";
            } else if (sheet.getStatus() == JudgingStatus.COMPLETED) {
                status = "DONE";
            }

            return StewardQueueEntryDto.builder()
                    .id(sheet.getId())
                    .catalogNumber(sheet.getCatalogNumber())
                    .catName(sheet.getCatEntry().getCat().getCatName())
                    .ems(sheet.getCatEntry().getCat().getEmsCode())
                    .sex(sheet.getCatEntry().getCat().getGender().toString())
                    .birthDate(sheet.getCatEntry().getCat().getBirthDate())
                    .status(status)
                    .urgency(hasCall ? activeCall.getUrgency().name() : "NORMAL")
                    .callingRecordId(hasCall ? activeCall.getId() : null)
                    .group(sheet.getCatEntry().getCat().getCatGroup())
                    .category(sheet.getCatEntry().getCat().getEmsCode() != null ? sheet.getCatEntry().getCat().getEmsCode().substring(0,1) : "I")
                    .breed(sheet.getCatEntry().getCat().getEmsCode() != null ? sheet.getCatEntry().getCat().getEmsCode().split(" ")[0] : "")
                    .judgeId(judgeId)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional
    public void updateSheetStatus(Long sheetId, String status) {
        JudgingSheet sheet = judgingSheetRepository.findById(sheetId).orElseThrow();
        if ("DONE".equals(status)) {
            sheet.setStatus(JudgingStatus.COMPLETED);
        } else if ("ABSENT".equals(status)) {
            sheet.setStatus(JudgingStatus.ABSENT);
        } else if ("JUDGING".equals(status)) {
            sheet.setStatus(JudgingStatus.IN_PROGRESS);
        } else if ("READY".equals(status)) {
            sheet.setStatus(JudgingStatus.READY);
        } else if ("WAITING".equals(status)) {
            sheet.setStatus(JudgingStatus.PENDING);
        }
        judgingSheetRepository.save(sheet);
    }

    @Transactional
    public void togglePause(Long showId, Long judgeId, boolean isPaused) {
        Optional<StewardLock> lockOpt = stewardLockRepository.findByShowIdAndJudgeId(showId, judgeId);
        if (lockOpt.isPresent()) {
            StewardLock lock = lockOpt.get();
            lock.setIsPaused(isPaused);
            stewardLockRepository.save(lock);
        }
    }
}