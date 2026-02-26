package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.CallingRequestDto;
import com.gpfteam.catshow.catshow_backend.model.CallingRecord;
import com.gpfteam.catshow.catshow_backend.model.enums.UrgencyLevel;
import com.gpfteam.catshow.catshow_backend.service.CallingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/calling")
@RequiredArgsConstructor
public class CallingController {

    private final CallingService callingService;

    @PostMapping("/call")
    public ResponseEntity<CallingRecord> callCat(@RequestBody CallingRequestDto request) {
        return ResponseEntity.ok(callingService.callCatToTable(request));
    }

    @PatchMapping("/{id}/urgency")
    public ResponseEntity<CallingRecord> updateUrgency(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        UrgencyLevel level = UrgencyLevel.valueOf(payload.get("urgency"));
        return ResponseEntity.ok(callingService.updateUrgency(id, level));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCall(@PathVariable Long id) {
        callingService.removeCall(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<CallingRecord>> getActiveCalls(@PathVariable Long showId) {
        return ResponseEntity.ok(callingService.getActiveCallsForShow(showId));
    }

    @GetMapping("/show/{showId}/board-state")
    public ResponseEntity<List<com.gpfteam.catshow.catshow_backend.dto.PublicBoardDto>> getBoardState(@PathVariable Long showId) {
        return ResponseEntity.ok(callingService.getPublicBoardState(showId));
    }
}