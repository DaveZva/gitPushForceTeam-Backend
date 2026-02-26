package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.dto.CallingRequestDto;
import com.gpfteam.catshow.catshow_backend.dto.PublicBoardDto;
import com.gpfteam.catshow.catshow_backend.model.CallingRecord;
import com.gpfteam.catshow.catshow_backend.model.Judge;
import com.gpfteam.catshow.catshow_backend.model.JudgingSheet;
import com.gpfteam.catshow.catshow_backend.model.StewardLock;
import com.gpfteam.catshow.catshow_backend.model.enums.JudgingStatus;
import com.gpfteam.catshow.catshow_backend.model.enums.UrgencyLevel;
import com.gpfteam.catshow.catshow_backend.repository.CallingRecordRepository;
import com.gpfteam.catshow.catshow_backend.repository.JudgeRepository;
import com.gpfteam.catshow.catshow_backend.repository.JudgingSheetRepository;
import com.gpfteam.catshow.catshow_backend.repository.StewardLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CallingService {

    private final CallingRecordRepository callingRecordRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final JudgeRepository judgeRepository;
    private final JudgingSheetRepository judgingSheetRepository;
    private final StewardLockRepository stewardLockRepository;

    public CallingRecord callCatToTable(CallingRequestDto request) {
        CallingRecord record = CallingRecord.builder()
                .showId(request.getShowId())
                .tableNo(request.getTableNo())
                .judgeName(request.getJudgeName())
                .catNumber(request.getCatNumber())
                .category(request.getCategory())
                .urgency(request.getUrgency() != null ? request.getUrgency() : UrgencyLevel.NORMAL)
                .calledAt(LocalDateTime.now())
                .build();

        CallingRecord saved = callingRecordRepository.save(record);
        broadcastShowBoardUpdate(request.getShowId());
        return saved;
    }

    public CallingRecord updateUrgency(Long id, UrgencyLevel newUrgency) {
        CallingRecord record = callingRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        record.setUrgency(newUrgency);
        record.setCalledAt(LocalDateTime.now());
        CallingRecord updated = callingRecordRepository.save(record);

        broadcastShowBoardUpdate(record.getShowId());
        return updated;
    }

    public void removeCall(Long id) {
        CallingRecord record = callingRecordRepository.findById(id).orElse(null);
        if (record != null) {
            Long showId = record.getShowId();
            callingRecordRepository.delete(record);
            broadcastShowBoardUpdate(showId);
        }
    }

    public List<CallingRecord> getActiveCallsForShow(Long showId) {
        return callingRecordRepository.findByShowId(showId);
    }

    private void broadcastShowBoardUpdate(Long showId) {
        List<CallingRecord> activeCalls = getActiveCallsForShow(showId);
        messagingTemplate.convertAndSend("/topic/show/" + showId + "/board", activeCalls);
    }

    public List<PublicBoardDto> getPublicBoardState(Long showId) {
        List<StewardLock> locks = stewardLockRepository.findByShowId(showId);
        List<CallingRecord> activeCalls = callingRecordRepository.findByShowId(showId);
        List<JudgingSheet> allSheets = judgingSheetRepository.findByShowIdAndDay(showId, "SATURDAY");

        return locks.stream()
                .filter(lock -> lock.getTableNumber() != null)
                .map(lock -> {
                    Judge judge = judgeRepository.findById(lock.getJudgeId()).orElse(null);
                    if (judge == null) return null;

                    String tableNo = "T" + lock.getTableNumber();

                    CallingRecord activeCall = activeCalls.stream()
                            .filter(c -> c.getTableNo() != null && c.getTableNo().equals(tableNo))
                            .findFirst()
                            .orElse(null);

                    PublicBoardDto.BoardCatDto currentCat = null;
                    if (activeCall != null) {
                        currentCat = PublicBoardDto.BoardCatDto.builder()
                                .catalogNumber(activeCall.getCatNumber())
                                .ems("...")
                                .type(activeCall.getCategory())
                                .urgency(activeCall.getUrgency().name())
                                .build();

                        Optional<JudgingSheet> sheetOpt = allSheets.stream()
                                .filter(s -> s.getCatalogNumber().equals(activeCall.getCatNumber()))
                                .findFirst();
                        if (sheetOpt.isPresent()) {
                            currentCat.setEms(sheetOpt.get().getCatEntry().getCat().getEmsCode());
                        }
                    }

                    List<PublicBoardDto.BoardCatDto> preparingCats = allSheets.stream()
                            .filter(s -> s.getJudge().getId().equals(judge.getId()) && s.getStatus() == JudgingStatus.READY)
                            .sorted(Comparator.comparing(JudgingSheet::getCatalogNumber))
                            .limit(2)
                            .map(s -> PublicBoardDto.BoardCatDto.builder()
                                    .catalogNumber(s.getCatalogNumber())
                                    .ems(s.getCatEntry().getCat().getEmsCode())
                                    .type("NORMAL")
                                    .urgency("NORMAL")
                                    .build())
                            .collect(Collectors.toList());

                    List<PublicBoardDto.BoardCatDto> waitingCats = allSheets.stream()
                            .filter(s -> s.getJudge().getId().equals(judge.getId()) && s.getStatus() == JudgingStatus.PENDING)
                            .sorted(Comparator.comparing(JudgingSheet::getCatalogNumber))
                            .limit(2)
                            .map(s -> PublicBoardDto.BoardCatDto.builder()
                                    .catalogNumber(s.getCatalogNumber())
                                    .ems(s.getCatEntry().getCat().getEmsCode())
                                    .type("NORMAL")
                                    .urgency("NORMAL")
                                    .build())
                            .collect(Collectors.toList());

                    return PublicBoardDto.builder()
                            .judgeId(judge.getId())
                            .judgeName(judge.getFirstName() + " " + judge.getLastName())
                            .tableNo(tableNo)
                            .currentCat(currentCat)
                            .preparingCats(preparingCats)
                            .waitingCats(waitingCats)
                            .build();
                })
                .filter(java.util.Objects::nonNull)
                .sorted(Comparator.comparing(dto -> Integer.parseInt(dto.getTableNo().replace("T", ""))))
                .limit(8)
                .collect(Collectors.toList());
    }
}