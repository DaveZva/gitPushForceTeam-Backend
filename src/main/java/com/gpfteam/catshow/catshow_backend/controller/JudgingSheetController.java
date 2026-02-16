package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.JudgeWorkloadDto;
import com.gpfteam.catshow.catshow_backend.dto.JudgingSheetDto;
import com.gpfteam.catshow.catshow_backend.service.JudgingSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/secretariat/{showId}/judging")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JudgingSheetController {

    private final JudgingSheetService judgingSheetService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateSheets(@PathVariable Long showId) {
        try {
            judgingSheetService.generateJudgingSheetsForShow(showId);
            return ResponseEntity.ok("Sheets generated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/generate-day")
    public ResponseEntity<String> generateForDay(
            @PathVariable Long showId,
            @RequestParam String day) {
        try {
            judgingSheetService.generateForDay(showId, day);
            return ResponseEntity.ok("Sheets generated for " + day);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/judges/{judgeId}/regenerate")
    public ResponseEntity<String> regenerateForJudge(
            @PathVariable Long showId,
            @PathVariable Long judgeId) {
        judgingSheetService.regenerateJudgingSheetsForJudge(showId, judgeId);
        return ResponseEntity.ok("Sheets regenerated");
    }

    @GetMapping("/judges/{judgeId}/sheets")
    public ResponseEntity<List<JudgingSheetDto>> getJudgeSheets(
            @PathVariable Long showId,
            @PathVariable Long judgeId,
            @RequestParam(required = false) String day) {
        List<JudgingSheetDto> sheets = judgingSheetService.getJudgeSheets(showId, judgeId, day);
        return ResponseEntity.ok(sheets);
    }

    @GetMapping("/workload")
    public ResponseEntity<List<JudgeWorkloadDto>> getWorkloadStats(
            @PathVariable Long showId,
            @RequestParam String day) {
        List<JudgeWorkloadDto> stats = judgingSheetService.getWorkloadStats(showId, day);
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/rebalance")
    public ResponseEntity<String> rebalanceWorkload(
            @PathVariable Long showId,
            @RequestParam String day) {
        judgingSheetService.generateForDay(showId, day);
        return ResponseEntity.ok("Workload rebalanced");
    }
}