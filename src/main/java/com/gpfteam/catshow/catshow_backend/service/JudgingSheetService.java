package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.dto.BreedDistributionDto;
import com.gpfteam.catshow.catshow_backend.dto.JudgeWorkloadDto;
import com.gpfteam.catshow.catshow_backend.dto.JudgingSheetDto;
import com.gpfteam.catshow.catshow_backend.model.*;
import com.gpfteam.catshow.catshow_backend.model.enums.JudgingStatus;
import com.gpfteam.catshow.catshow_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JudgingSheetService {

    private final JudgingSheetRepository judgingSheetRepository;
    private final JudgeAssignmentService judgeAssignmentService;
    private final ShowRepository showRepository;
    private final JudgeRepository judgeRepository;
    private final PdfGenerationService pdfGenerationService;

    @Transactional
    public void generateJudgingSheetsForShow(Long showId) {
        Show show = showRepository.findById(showId).orElseThrow();
        List<String> days = getDaysFromShow(show);
        for (String day : days) {
            generateForDay(showId, day);
        }
    }

    @Transactional
    public void generateForDay(Long showId, String day) {
        log.info("Mažu staré posuzovací listy pro showId: {}, den: {}", showId, day);
        judgingSheetRepository.deleteByShowIdAndDay(showId, day);
        judgingSheetRepository.flush();

        Map<Long, List<RegistrationEntry>> distribution =
                judgeAssignmentService.distributeWorkloadEvenly(showId, day);

        log.info("Distribuce dokončena. Počet posuzovatelů s přiřazenou prací: {}", distribution.size());

        if (distribution.isEmpty()) {
            return;
        }

        Show show = showRepository.findById(showId).orElseThrow();

        Set<Long> judgeIds = distribution.keySet();
        Map<Long, Judge> judgeMap = judgeRepository.findAllById(judgeIds)
                .stream()
                .collect(Collectors.toMap(Judge::getId, j -> j));

        LocalDateTime now = LocalDateTime.now();
        List<JudgingSheet> sheetsToSave = new ArrayList<>();

        for (Map.Entry<Long, List<RegistrationEntry>> entry : distribution.entrySet()) {
            Judge judge = judgeMap.get(entry.getKey());
            if (judge == null) {
                log.warn("Soudce ID {} nenalezen, přeskakuji", entry.getKey());
                continue;
            }
            log.info("Připravuji {} listů pro soudce ID: {}", entry.getValue().size(), entry.getKey());

            for (RegistrationEntry catEntry : entry.getValue()) {
                sheetsToSave.add(JudgingSheet.builder()
                        .show(show)
                        .judge(judge)
                        .catEntry(catEntry)
                        .day(day)
                        .status(JudgingStatus.PENDING)
                        .catalogNumber(catEntry.getCatalogNumber())
                        .createdAt(now)
                        .build());
            }
        }

        // Jeden batch INSERT místo N jednotlivých INSERT volání
        judgingSheetRepository.saveAll(sheetsToSave);
        log.info("Uloženo {} posuzovacích listů pro den {}", sheetsToSave.size(), day);
    }

    @Transactional
    public void regenerateJudgingSheetsForJudge(Long showId, Long judgeId) {
        judgingSheetRepository.deleteByShowIdAndJudgeId(showId, judgeId);
        judgingSheetRepository.flush();

        Show show = showRepository.findById(showId).orElseThrow();
        Judge judge = judgeRepository.findById(judgeId).orElseThrow();
        List<String> days = getDaysFromShow(show);

        List<JudgingSheet> sheetsToSave = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (String day : days) {
            Map<Long, List<RegistrationEntry>> distribution =
                    judgeAssignmentService.distributeWorkloadEvenly(showId, day);

            if (distribution.containsKey(judgeId)) {
                for (RegistrationEntry catEntry : distribution.get(judgeId)) {
                    sheetsToSave.add(JudgingSheet.builder()
                            .show(show)
                            .judge(judge)
                            .catEntry(catEntry)
                            .day(day)
                            .status(JudgingStatus.PENDING)
                            .catalogNumber(catEntry.getCatalogNumber())
                            .createdAt(now)
                            .build());
                }
            }
        }

        judgingSheetRepository.saveAll(sheetsToSave);
        log.info("Regenerováno {} listů pro soudce {} ", sheetsToSave.size(), judgeId);
    }

    public List<JudgeWorkloadDto> getWorkloadStats(Long showId, String day) {
        List<JudgingSheet> sheets = judgingSheetRepository.findByShowIdAndDay(showId, day);

        Map<Judge, List<JudgingSheet>> byJudge = sheets.stream()
                .collect(Collectors.groupingBy(JudgingSheet::getJudge));

        return byJudge.entrySet().stream().map(e -> {
            Judge judge = e.getKey();
            List<JudgingSheet> judgeSheets = e.getValue();

            Map<String, Long> breedCounts = judgeSheets.stream()
                    .collect(Collectors.groupingBy(
                            s -> s.getCatEntry().getCat().getEmsCode().split(" ")[0],
                            Collectors.counting()));

            List<BreedDistributionDto> breedDistribution = breedCounts.entrySet().stream()
                    .map(be -> BreedDistributionDto.builder()
                            .code(be.getKey())
                            .name(getBreedName(be.getKey()))
                            .count(be.getValue().intValue())
                            .build())
                    .collect(Collectors.toList());

            return JudgeWorkloadDto.builder()
                    .judgeId(judge.getId())
                    .judgeName(judge.getFirstName() + " " + judge.getLastName())
                    .qualifications(judge.getValidGroups())
                    .catsCount(judgeSheets.size())
                    .breedDistribution(breedDistribution)
                    .build();
        }).collect(Collectors.toList());
    }

    public List<JudgingSheetDto> getJudgeSheets(Long showId, Long judgeId, String day) {
        List<JudgingSheet> sheets = judgingSheetRepository.findByShowIdAndJudgeIdAndDay(showId, judgeId, day);

        return sheets.stream()
                .map(sheet -> JudgingSheetDto.builder()
                        .id(sheet.getId())
                        .judgeId(sheet.getJudge().getId())
                        .catEntryId(sheet.getCatEntry().getId())
                        .catName(sheet.getCatEntry().getCat().getCatName())
                        .emsCode(sheet.getCatEntry().getCat().getEmsCode())
                        .catGroup(sheet.getCatEntry().getCat().getCatGroup())
                        .catalogNumber(sheet.getCatalogNumber())
                        .status(sheet.getStatus().name())
                        .day(sheet.getDay())
                        .build())
                .collect(Collectors.toList());
    }

    private List<String> getDaysFromShow(Show show) {
        List<String> days = new ArrayList<>();
        days.add("SATURDAY");
        days.add("SUNDAY");
        return days;
    }

    private String getBreedName(String code) {
        Map<String, String> breedNames = Map.ofEntries(
                Map.entry("EXO", "Exotic"),
                Map.entry("PER", "Persian"),
                Map.entry("MCO", "Maine Coon"),
                Map.entry("NEM", "Neva Masquerade"),
                Map.entry("SIB", "Siberian"),
                Map.entry("BSH", "British Shorthair"),
                Map.entry("BLH", "British Longhair")
        );
        return breedNames.getOrDefault(code, code);
    }

    public byte[] generatePdfForJudge(Long showId, Long judgeId, String day) throws IOException {
        List<JudgingSheet> sheets = judgingSheetRepository.findByShowIdAndJudgeIdAndDay(showId, judgeId, day);
        if (sheets.isEmpty()) {
            throw new RuntimeException("No judging sheets found for judge " + judgeId + " on " + day);
        }
        return pdfGenerationService.createJudgingSheetsPdf(sheets);
    }
}