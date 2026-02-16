package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.model.*;
import com.gpfteam.catshow.catshow_backend.repository.*;
import com.gpfteam.catshow.catshow_backend.util.EmsUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JudgeAssignmentService {

    private final ShowRepository showRepository;
    private final JudgeAssignmentRepository judgeAssignmentRepository;
    private final RegistrationEntryRepository registrationEntryRepository;
    private final JudgingSheetRepository judgingSheetRepository;

    @Transactional
    public Map<Long, List<RegistrationEntry>> distributeWorkloadEvenly(Long showId, String day) {
        Show show = showRepository.findById(showId).orElseThrow();
        List<Judge> availableJudges = show.getJudges();

        List<RegistrationEntry> catsToJudge = registrationEntryRepository.findByShowAndStatusConfirmed(showId, day);

        Map<String, List<RegistrationEntry>> catsByBreedGroup = groupCatsByBreedAndGroup(catsToJudge);

        Map<Long, List<RegistrationEntry>> judgeAssignments = new HashMap<>();
        Map<Long, Integer> judgeWorkload = new HashMap<>();
        for (Judge judge : availableJudges) {
            judgeAssignments.put(judge.getId(), new ArrayList<>());
            judgeWorkload.put(judge.getId(), 0);
        }

        List<JudgeAssignment> manualAssignments = judgeAssignmentRepository.findByShowIdAndDay(showId, day);

        for (JudgeAssignment assignment : manualAssignments) {
            List<RegistrationEntry> assignedCats = findCatsMatchingAssignment(catsToJudge, assignment);

            judgeAssignments.get(assignment.getJudge().getId()).addAll(assignedCats);
            judgeWorkload.put(assignment.getJudge().getId(),
                    judgeWorkload.get(assignment.getJudge().getId()) + assignedCats.size());

            catsToJudge.removeAll(assignedCats);
            catsByBreedGroup.values().forEach(list -> list.removeAll(assignedCats));
        }

        List<String> breedGroups = new ArrayList<>(catsByBreedGroup.keySet());
        breedGroups.sort((a, b) ->
                Integer.compare(
                        catsByBreedGroup.get(b).size(),
                        catsByBreedGroup.get(a).size()
                )
        );

        for (String breedGroup : breedGroups) {
            List<RegistrationEntry> cats = catsByBreedGroup.get(breedGroup);
            if (cats.isEmpty()) continue;

            List<Judge> qualifiedJudges = availableJudges.stream()
                    .filter(j -> isQualifiedForBreedGroup(j, breedGroup))
                    .collect(Collectors.toList());

            if (qualifiedJudges.isEmpty()) {
                throw new IllegalStateException("No qualified judge for group: " + breedGroup);
            }

            Judge selectedJudge = qualifiedJudges.stream()
                    .min(Comparator.comparingInt(j -> judgeWorkload.get(j.getId())))
                    .orElseThrow();

            judgeAssignments.get(selectedJudge.getId()).addAll(cats);
            judgeWorkload.put(selectedJudge.getId(),
                    judgeWorkload.get(selectedJudge.getId()) + cats.size());
        }

        balanceWorkloadIfNeeded(judgeAssignments, judgeWorkload, availableJudges);

        return judgeAssignments;
    }

    private Map<String, List<RegistrationEntry>> groupCatsByBreedAndGroup(List<RegistrationEntry> cats) {
        Map<String, List<RegistrationEntry>> grouped = new HashMap<>();

        for (RegistrationEntry entry : cats) {
            Cat cat = entry.getCat();
            String emsCode = cat.getEmsCode();
            String breedCode = emsCode.split(" ")[0];
            String group = cat.getCatGroup();

            String category = "CAT" + EmsUtility.getCategory(emsCode);
            String key = category + "-" + breedCode + "-" + (group != null ? group : "");

            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(entry);
        }

        return grouped;
    }

    private boolean isQualifiedForBreedGroup(Judge judge, String breedGroupKey) {
        String[] parts = breedGroupKey.split("-");
        String category = parts[0];

        int fifeCategory = Integer.parseInt(category.substring(3));

        List<String> validGroups = judge.getValidGroups();

        return validGroups.contains("CAT" + fifeCategory) ||
                validGroups.contains("ALL_BREEDS") ||
                validGroups.contains("AB");
    }

    private void balanceWorkloadIfNeeded(
            Map<Long, List<RegistrationEntry>> assignments,
            Map<Long, Integer> workload,
            List<Judge> judges) {

        double avgWorkload = workload.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);

        Long mostLoadedJudgeId = workload.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        Long leastLoadedJudgeId = workload.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (mostLoadedJudgeId == null || leastLoadedJudgeId == null) return;

        int maxLoad = workload.get(mostLoadedJudgeId);
        int minLoad = workload.get(leastLoadedJudgeId);

        if (maxLoad - minLoad > avgWorkload * 0.2) {

            Judge mostLoaded = judges.stream()
                    .filter(j -> j.getId().equals(mostLoadedJudgeId))
                    .findFirst().orElse(null);
            Judge leastLoaded = judges.stream()
                    .filter(j -> j.getId().equals(leastLoadedJudgeId))
                    .findFirst().orElse(null);

            if (mostLoaded == null || leastLoaded == null) return;

            List<RegistrationEntry> catsToMove = new ArrayList<>();
            List<RegistrationEntry> mostLoadedCats = assignments.get(mostLoadedJudgeId);

            for (RegistrationEntry cat : mostLoadedCats) {
                String breedGroup = getBreedGroupKey(cat);

                if (isQualifiedForBreedGroup(leastLoaded, breedGroup)) {
                    catsToMove.add(cat);

                    if (catsToMove.size() >= (maxLoad - minLoad) / 2) {
                        break;
                    }
                }
            }

            if (!catsToMove.isEmpty()) {
                assignments.get(mostLoadedJudgeId).removeAll(catsToMove);
                assignments.get(leastLoadedJudgeId).addAll(catsToMove);

                workload.put(mostLoadedJudgeId,
                        workload.get(mostLoadedJudgeId) - catsToMove.size());
                workload.put(leastLoadedJudgeId,
                        workload.get(leastLoadedJudgeId) + catsToMove.size());
            }
        }
    }

    private String getBreedGroupKey(RegistrationEntry entry) {
        Cat cat = entry.getCat();
        String emsCode = cat.getEmsCode();
        String breedCode = emsCode.split(" ")[0];
        String group = cat.getCatGroup();
        String category = "CAT" + EmsUtility.getCategory(emsCode);

        return category + "-" + breedCode + "-" + (group != null ? group : "");
    }

    private List<RegistrationEntry> findCatsMatchingAssignment(
            List<RegistrationEntry> entries,
            JudgeAssignment assignment) {

        return entries.stream()
                .filter(entry -> {
                    Cat cat = entry.getCat();
                    String emsCode = cat.getEmsCode();
                    String[] emsParts = emsCode.split(" ");
                    String breedCode = emsParts[0];
                    String colorCode = emsParts.length > 1 ? emsParts[1] : "";

                    String breedGroup = getBreedGroup(breedCode);
                    if (!assignment.getJudge().getValidGroups().contains(breedGroup)) {
                        return false;
                    }

                    if (assignment.getAssignedBreedCodes() != null &&
                            !assignment.getAssignedBreedCodes().isEmpty() &&
                            !assignment.getAssignedBreedCodes().contains(breedCode)) {
                        return false;
                    }

                    if (assignment.getAssignedColorCodes() != null &&
                            !assignment.getAssignedColorCodes().isEmpty() &&
                            !assignment.getAssignedColorCodes().contains(colorCode)) {
                        return false;
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    private String getBreedGroup(String breedCode) {
        return "CAT" + EmsUtility.getCategory(breedCode + " n");
    }
}