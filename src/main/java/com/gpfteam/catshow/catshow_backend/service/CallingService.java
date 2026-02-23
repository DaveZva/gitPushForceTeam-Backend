package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.dto.CallingRequestDto;
import com.gpfteam.catshow.catshow_backend.model.CallingRecord;
import com.gpfteam.catshow.catshow_backend.model.enums.UrgencyLevel;
import com.gpfteam.catshow.catshow_backend.repository.CallingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallingService {

    private final CallingRecordRepository callingRecordRepository;
    private final SimpMessagingTemplate messagingTemplate;

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
}