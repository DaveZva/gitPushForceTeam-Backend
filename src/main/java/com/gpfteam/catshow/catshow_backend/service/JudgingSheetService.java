package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.dto.BreedDistributionDto;
import com.gpfteam.catshow.catshow_backend.dto.JudgeWorkloadDto;
import com.gpfteam.catshow.catshow_backend.dto.JudgingSheetDto;
import com.gpfteam.catshow.catshow_backend.model.*;
import com.gpfteam.catshow.catshow_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JudgingSheetService {

    private final JudgingSheetRepository judgingSheetRepository;
    private final JudgeAssignmentService judgeAssignmentService;
    private final ShowRepository showRepository;
    private final JudgeRepository judgeRepository;

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
        judgingSheetRepository.deleteByShowIdAndDay(showId, day);

        Map<Long, List<RegistrationEntry>> distribution =
                judgeAssignmentService.distributeWorkloadEvenly(showId, day);

        if (distribution.isEmpty()) {
            return;
        }

        Show show = showRepository.findById(showId).orElseThrow();

        for (Map.Entry<Long, List<RegistrationEntry>> entry : distribution.entrySet()) {
            Judge judge = judgeRepository.findById(entry.getKey()).orElseThrow();

            for (RegistrationEntry catEntry : entry.getValue()) {
                JudgingSheet sheet = JudgingSheet.builder()
                        .show(show)
                        .judge(judge)
                        .catEntry(catEntry)
                        .day(day)
                        .status(JudgingSheet.JudgingStatus.PENDING)
                        .catalogNumber(catEntry.getCatalogNumber())
                        .createdAt(LocalDateTime.now())
                        .build();

                judgingSheetRepository.save(sheet);
            }
        }
    }

    @Transactional
    public void regenerateJudgingSheetsForJudge(Long showId, Long judgeId) {
        judgingSheetRepository.deleteByShowIdAndJudgeId(showId, judgeId);

        Show show = showRepository.findById(showId).orElseThrow();
        List<String> days = getDaysFromShow(show);

        for (String day : days) {
            Map<Long, List<RegistrationEntry>> distribution =
                    judgeAssignmentService.distributeWorkloadEvenly(showId, day);

            if (distribution.containsKey(judgeId)) {
                Judge judge = judgeRepository.findById(judgeId).orElseThrow();

                for (RegistrationEntry catEntry : distribution.get(judgeId)) {
                    JudgingSheet sheet = JudgingSheet.builder()
                            .show(show)
                            .judge(judge)
                            .catEntry(catEntry)
                            .day(day)
                            .status(JudgingSheet.JudgingStatus.PENDING)
                            .catalogNumber(catEntry.getCatalogNumber())
                            .createdAt(LocalDateTime.now())
                            .build();

                    judgingSheetRepository.save(sheet);
                }
            }
        }
    }

    public List<JudgeWorkloadDto> getWorkloadStats(Long showId, String day) {
        Map<Long, List<RegistrationEntry>> distribution =
                judgeAssignmentService.distributeWorkloadEvenly(showId, day);

        List<JudgeWorkloadDto> stats = new ArrayList<>();

        for (Map.Entry<Long, List<RegistrationEntry>> entry : distribution.entrySet()) {
            Judge judge = judgeRepository.findById(entry.getKey()).orElseThrow();
            List<RegistrationEntry> cats = entry.getValue();

            Map<String, Long> breedCounts = cats.stream()
                    .collect(Collectors.groupingBy(
                            e -> e.getCat().getEmsCode().split(" ")[0],
                            Collectors.counting()
                    ));

            List<BreedDistributionDto> breedDistribution = breedCounts.entrySet().stream()
                    .map(e -> BreedDistributionDto.builder()
                            .code(e.getKey())
                            .name(getBreedName(e.getKey()))
                            .count(e.getValue().intValue())
                            .build())
                    .collect(Collectors.toList());

            stats.add(JudgeWorkloadDto.builder()
                    .judgeId(judge.getId())
                    .judgeName(judge.getFirstName() + " " + judge.getLastName())
                    .qualifications(judge.getValidGroups())
                    .catsCount(cats.size())
                    .breedDistribution(breedDistribution)
                    .build());
        }

        return stats;
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
}