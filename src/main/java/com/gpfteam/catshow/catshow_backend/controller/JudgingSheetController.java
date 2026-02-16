package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.JudgeWorkloadDto;
import com.gpfteam.catshow.catshow_backend.dto.JudgingSheetDto;
import com.gpfteam.catshow.catshow_backend.service.JudgingSheetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/secretariat/shows/{showId}/judging")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class JudgingSheetController {

    private final JudgingSheetService judgingSheetService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateSheets(@PathVariable Long showId) {
        try {
            judgingSheetService.generateJudgingSheetsForShow(showId);
            return ResponseEntity.ok("Sheets generated successfully");
        } catch (IllegalStateException e) {
            log.warn("Logic error generating sheets: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error generating sheets", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Unexpected error: " + e.getMessage()));
        }
    }

    @PostMapping("/generate-day")
    public ResponseEntity<?> generateForDay(
            @PathVariable Long showId,
            @RequestParam String day) {
        try {
            judgingSheetService.generateForDay(showId, day);
            return ResponseEntity.ok("Sheets generated for " + day);
        } catch (Exception e) {
            log.error("Error generating sheets for day", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/judges/{judgeId}/regenerate")
    public ResponseEntity<?> regenerateForJudge(
            @PathVariable Long showId,
            @PathVariable Long judgeId) {
        try {
            judgingSheetService.regenerateJudgingSheetsForJudge(showId, judgeId);
            return ResponseEntity.ok("Sheets regenerated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
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
    public ResponseEntity<?> rebalanceWorkload(
            @PathVariable Long showId,
            @RequestParam String day) {
        try {
            judgingSheetService.generateForDay(showId, day);
            return ResponseEntity.ok("Workload rebalanced");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/judges/{judgeId}/sheets/pdf")
    public ResponseEntity<byte[]> downloadJudgeSheetsPdf(
            @PathVariable Long showId,
            @PathVariable Long judgeId,
            @RequestParam String day) {
        try {
            byte[] pdfContent = judgingSheetService.generatePdfForJudge(showId, judgeId, day);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "attachment; filename=judging_sheets_" + judgeId + ".pdf")
                    .body(pdfContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    static class ErrorResponse {
        public String message;
        public ErrorResponse(String message) { this.message = message; }
    }
}