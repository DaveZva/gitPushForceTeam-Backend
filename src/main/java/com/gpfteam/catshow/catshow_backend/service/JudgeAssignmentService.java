package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.model.*;
import com.gpfteam.catshow.catshow_backend.repository.*;
import com.gpfteam.catshow.catshow_backend.util.EmsUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JudgeAssignmentService {

    private final ShowRepository showRepository;
    private final JudgeAssignmentRepository judgeAssignmentRepository;
    private final RegistrationEntryRepository registrationEntryRepository;

    @Transactional
    public Map<Long, List<RegistrationEntry>> distributeWorkloadEvenly(Long showId, String day) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found: " + showId));

        List<Judge> availableJudges = show.getJudges();
        if (availableJudges == null || availableJudges.isEmpty()) {
            log.error("No judges assigned to show {}", showId);
            return new HashMap<>();
        }

        log.info("Starting distribution for showId: {}, day: {}", showId, day);

        List<RegistrationEntry> allEntries = registrationEntryRepository.findByShowId(showId);

        List<RegistrationEntry> catsToJudge = allEntries.stream()
                .filter(e -> {
                    Registration reg = e.getRegistration();
                    if (reg == null) return false;

                    boolean isConfirmed = reg.getStatus() == Registration.RegistrationStatus.CONFIRMED;

                    String regDays = reg.getDays() != null ? reg.getDays().toUpperCase() : "";
                    boolean dayMatches = regDays.contains(day.toUpperCase()) || regDays.contains("BOTH");

                    return isConfirmed && dayMatches;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        log.info("Found {} confirmed cats for day {}", catsToJudge.size(), day);

        if (catsToJudge.isEmpty()) {
            log.warn("No confirmed cats found for day {}", day);
            return new HashMap<>();
        }

        Map<Long, List<RegistrationEntry>> judgeAssignments = new HashMap<>();
        Map<Long, Integer> judgeWorkload = new HashMap<>();
        for (Judge judge : availableJudges) {
            judgeAssignments.put(judge.getId(), new ArrayList<>());
            judgeWorkload.put(judge.getId(), 0);
        }

        List<JudgeAssignment> manualAssignments = judgeAssignmentRepository.findByShowIdAndDay(showId, day);
        for (JudgeAssignment assignment : manualAssignments) {
            List<RegistrationEntry> assignedCats = findCatsMatchingAssignment(catsToJudge, assignment);
            if (!assignedCats.isEmpty()) {
                Long judgeId = assignment.getJudge().getId();
                if (judgeAssignments.containsKey(judgeId)) {
                    judgeAssignments.get(judgeId).addAll(assignedCats);
                    judgeWorkload.put(judgeId, judgeWorkload.get(judgeId) + assignedCats.size());
                    catsToJudge.removeAll(assignedCats);
                }
            }
        }

        Map<String, List<RegistrationEntry>> catsByBreedGroup = groupCatsByBreedAndGroup(catsToJudge);
        List<String> breedGroups = new ArrayList<>(catsByBreedGroup.keySet());
        breedGroups.sort((a, b) -> Integer.compare(catsByBreedGroup.get(b).size(), catsByBreedGroup.get(a).size()));

        for (String breedGroup : breedGroups) {
            List<RegistrationEntry> catsInGroup = catsByBreedGroup.get(breedGroup);
            if (catsInGroup.isEmpty()) continue;

            List<Judge> qualifiedJudges = availableJudges.stream()
                    .filter(j -> isQualifiedForBreedGroup(j, breedGroup))
                    .collect(Collectors.toList());

            if (qualifiedJudges.isEmpty()) {
                log.error("No qualified judge found for group: {}", breedGroup);
                continue;
            }

            Judge selectedJudge = qualifiedJudges.stream()
                    .min(Comparator.comparingInt(j -> judgeWorkload.get(j.getId())))
                    .orElseThrow();

            judgeAssignments.get(selectedJudge.getId()).addAll(catsInGroup);
            judgeWorkload.put(selectedJudge.getId(), judgeWorkload.get(selectedJudge.getId()) + catsInGroup.size());
        }

        balanceWorkloadIfNeeded(judgeAssignments, judgeWorkload, availableJudges);

        return judgeAssignments;
    }

    private Map<String, List<RegistrationEntry>> groupCatsByBreedAndGroup(List<RegistrationEntry> cats) {
        Map<String, List<RegistrationEntry>> grouped = new HashMap<>();
        for (RegistrationEntry entry : cats) {
            String key = getBreedGroupKey(entry);
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(entry);
        }
        return grouped;
    }

    private boolean isQualifiedForBreedGroup(Judge judge, String breedGroupKey) {
        if (judge.getValidGroups() == null) return false;

        String categoryNumber = breedGroupKey.split("-")[0].replace("CAT", "");
        List<String> validGroups = judge.getValidGroups();

        return validGroups.contains(categoryNumber) ||
                validGroups.contains("ALL_BREEDS") ||
                validGroups.contains("AB") ||
                validGroups.contains(breedGroupKey.split("-")[0]);
    }

    private void balanceWorkloadIfNeeded(Map<Long, List<RegistrationEntry>> assignments, Map<Long, Integer> workload, List<Judge> judges) {
        if (workload.size() < 2) return;

        double avgWorkload = workload.values().stream().mapToInt(Integer::intValue).average().orElse(0);
        Long mostLoadedId = Collections.max(workload.entrySet(), Map.Entry.comparingByValue()).getKey();
        Long leastLoadedId = Collections.min(workload.entrySet(), Map.Entry.comparingByValue()).getKey();

        int maxLoad = workload.get(mostLoadedId);
        int minLoad = workload.get(leastLoadedId);

        if (maxLoad - minLoad > Math.max(3, avgWorkload * 0.2)) {
            Judge leastLoadedJudge = judges.stream().filter(j -> j.getId().equals(leastLoadedId)).findFirst().orElse(null);
            if (leastLoadedJudge == null) return;

            List<RegistrationEntry> mostLoadedCats = new ArrayList<>(assignments.get(mostLoadedId));
            List<RegistrationEntry> catsToMove = new ArrayList<>();

            for (RegistrationEntry cat : mostLoadedCats) {
                if (isQualifiedForBreedGroup(leastLoadedJudge, getBreedGroupKey(cat))) {
                    catsToMove.add(cat);
                    if (catsToMove.size() >= (maxLoad - minLoad) / 2) break;
                }
            }

            if (!catsToMove.isEmpty()) {
                assignments.get(mostLoadedId).removeAll(catsToMove);
                assignments.get(leastLoadedId).addAll(catsToMove);
                workload.put(mostLoadedId, workload.get(mostLoadedId) - catsToMove.size());
                workload.put(leastLoadedId, workload.get(leastLoadedId) + catsToMove.size());
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

    private List<RegistrationEntry> findCatsMatchingAssignment(List<RegistrationEntry> entries, JudgeAssignment assignment) {
        if (assignment.getJudge() == null) return Collections.emptyList();

        return entries.stream()
                .filter(entry -> {
                    String breedGroupKey = getBreedGroupKey(entry);
                    if (!isQualifiedForBreedGroup(assignment.getJudge(), breedGroupKey)) return false;

                    String breedCode = entry.getCat().getEmsCode().split(" ")[0];
                    if (assignment.getAssignedBreedCodes() != null && !assignment.getAssignedBreedCodes().isEmpty()
                            && !assignment.getAssignedBreedCodes().contains(breedCode)) return false;

                    return true;
                })
                .collect(Collectors.toList());
    }
}