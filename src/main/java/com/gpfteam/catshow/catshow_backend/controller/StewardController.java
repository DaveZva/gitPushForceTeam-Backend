package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.StewardJudgeDto;
import com.gpfteam.catshow.catshow_backend.dto.StewardQueueEntryDto;
import com.gpfteam.catshow.catshow_backend.service.StewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/steward")
@RequiredArgsConstructor
public class StewardController {

    private final StewardService stewardService;

    @GetMapping("/shows/{showId}/judges")
    public ResponseEntity<List<StewardJudgeDto>> getJudges(@PathVariable Long showId, Authentication auth) {
        return ResponseEntity.ok(stewardService.getJudgesForShow(showId, auth.getName()));
    }

    @PostMapping("/shows/{showId}/judges/{judgeId}/lock")
    public ResponseEntity<Map<String, Boolean>> lockJudge(
            @PathVariable Long showId,
            @PathVariable Long judgeId,
            @RequestBody Map<String, Integer> payload,
            Authentication auth) {

        Integer tableNumber = payload.get("tableNumber");
        boolean success = stewardService.lockJudge(showId, judgeId, auth.getName(), tableNumber);
        return ResponseEntity.ok(Map.of("success", success));
    }

    @PostMapping("/shows/{showId}/judges/{judgeId}/unlock")
    public ResponseEntity<Void> unlockJudge(@PathVariable Long showId, @PathVariable Long judgeId, Authentication auth) {
        stewardService.unlockJudge(showId, judgeId, auth.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shows/{showId}/judges/{judgeId}/queue")
    public ResponseEntity<List<StewardQueueEntryDto>> getQueue(@PathVariable Long showId, @PathVariable Long judgeId) {
        return ResponseEntity.ok(stewardService.getQueue(showId, judgeId));
    }

    @PatchMapping("/sheets/{sheetId}/status")
    public ResponseEntity<Void> updateSheetStatus(@PathVariable Long sheetId, @RequestBody Map<String, String> payload) {
        stewardService.updateSheetStatus(sheetId, payload.get("status"));
        return ResponseEntity.ok().build();
    }
}