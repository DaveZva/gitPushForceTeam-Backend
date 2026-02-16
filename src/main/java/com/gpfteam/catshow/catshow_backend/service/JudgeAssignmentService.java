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
            throw new IllegalStateException("K výstavě nejsou přiřazeni žádní rozhodčí (No judges assigned to show).");
        }

        List<RegistrationEntry> catsToJudge = registrationEntryRepository.findByShowAndStatusConfirmed(showId, day);
        if (catsToJudge.isEmpty()) {
            log.warn("No cats found for show {} and day {}", showId, day);
            return new HashMap<>();
        }

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

            if (!assignedCats.isEmpty()) {
                Long judgeId = assignment.getJudge().getId();
                if (judgeAssignments.containsKey(judgeId)) {
                    judgeAssignments.get(judgeId).addAll(assignedCats);
                    judgeWorkload.put(judgeId, judgeWorkload.get(judgeId) + assignedCats.size());

                    catsToJudge.removeAll(assignedCats);
                    catsByBreedGroup.values().forEach(list -> list.removeAll(assignedCats));
                }
            }
        }

        List<String> breedGroups = new ArrayList<>(catsByBreedGroup.keySet());
        breedGroups.sort((a, b) -> Integer.compare(catsByBreedGroup.get(b).size(), catsByBreedGroup.get(a).size()));

        for (String breedGroup : breedGroups) {
            List<RegistrationEntry> cats = catsByBreedGroup.get(breedGroup);
            if (cats.isEmpty()) continue;

            List<Judge> qualifiedJudges = availableJudges.stream()
                    .filter(j -> isQualifiedForBreedGroup(j, breedGroup))
                    .collect(Collectors.toList());

            if (qualifiedJudges.isEmpty()) {
                throw new IllegalStateException("Nenalezen žádný kvalifikovaný rozhodčí pro kategorii: " + breedGroup +
                        ". Zkontrolujte nastavení skupin u rozhodčích.");
            }

            Judge selectedJudge = qualifiedJudges.stream()
                    .min(Comparator.comparingInt(j -> judgeWorkload.get(j.getId())))
                    .orElseThrow();

            judgeAssignments.get(selectedJudge.getId()).addAll(cats);
            judgeWorkload.put(selectedJudge.getId(), judgeWorkload.get(selectedJudge.getId()) + cats.size());
        }

        balanceWorkloadIfNeeded(judgeAssignments, judgeWorkload, availableJudges);

        return judgeAssignments;
    }

    private Map<String, List<RegistrationEntry>> groupCatsByBreedAndGroup(List<RegistrationEntry> cats) {
        Map<String, List<RegistrationEntry>> grouped = new HashMap<>();

        for (RegistrationEntry entry : cats) {
            try {
                Cat cat = entry.getCat();
                String emsCode = cat.getEmsCode();
                String breedCode = emsCode.split(" ")[0];
                String group = cat.getCatGroup();

                int categoryNum = EmsUtility.getCategory(emsCode);
                String category = "CAT" + categoryNum;

                String key = category + "-" + breedCode + "-" + (group != null ? group : "");

                grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(entry);
            } catch (Exception e) {
                log.error("Error grouping cat ID {}: {}", entry.getCat().getId(), e.getMessage());
            }
        }

        return grouped;
    }

    private boolean isQualifiedForBreedGroup(Judge judge, String breedGroupKey) {
        if (judge.getValidGroups() == null) return false;

        String[] parts = breedGroupKey.split("-");
        String categoryStr = parts[0];

        String categoryNumber = categoryStr.replace("CAT", "");

        List<String> validGroups = judge.getValidGroups();

        return validGroups.contains(categoryNumber) ||
                validGroups.contains(categoryStr) ||
                validGroups.contains("Category " + categoryNumber) ||
                validGroups.contains("ALL_BREEDS") ||
                validGroups.contains("AB");
    }

    private void balanceWorkloadIfNeeded(
            Map<Long, List<RegistrationEntry>> assignments,
            Map<Long, Integer> workload,
            List<Judge> judges) {

        if (workload.isEmpty()) return;

        double avgWorkload = workload.values().stream().mapToInt(Integer::intValue).average().orElse(0);

        Long mostLoadedId = Collections.max(workload.entrySet(), Map.Entry.comparingByValue()).getKey();
        Long leastLoadedId = Collections.min(workload.entrySet(), Map.Entry.comparingByValue()).getKey();

        int maxLoad = workload.get(mostLoadedId);
        int minLoad = workload.get(leastLoadedId);

        if (maxLoad - minLoad > Math.max(2, avgWorkload * 0.2)) {
            Judge mostLoadedJudge = judges.stream().filter(j -> j.getId().equals(mostLoadedId)).findFirst().orElse(null);
            Judge leastLoadedJudge = judges.stream().filter(j -> j.getId().equals(leastLoadedId)).findFirst().orElse(null);

            if (mostLoadedJudge == null || leastLoadedJudge == null) return;

            List<RegistrationEntry> catsToMove = new ArrayList<>();
            List<RegistrationEntry> sourceCats = new ArrayList<>(assignments.get(mostLoadedId));

            for (RegistrationEntry cat : sourceCats) {
                String breedGroupKey = getBreedGroupKey(cat);

                if (isQualifiedForBreedGroup(leastLoadedJudge, breedGroupKey)) {
                    catsToMove.add(cat);
                    if (catsToMove.size() >= (maxLoad - minLoad) / 2) {
                        break;
                    }
                }
            }

            if (!catsToMove.isEmpty()) {
                assignments.get(mostLoadedId).removeAll(catsToMove);
                assignments.get(leastLoadedId).addAll(catsToMove);

                workload.put(mostLoadedId, workload.get(mostLoadedId) - catsToMove.size());
                workload.put(leastLoadedId, workload.get(leastLoadedId) + catsToMove.size());

                log.info("Rebalanced {} cats from judge {} to judge {}",
                        catsToMove.size(), mostLoadedJudge.getLastName(), leastLoadedJudge.getLastName());
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

        if (assignment.getJudge() == null || assignment.getJudge().getValidGroups() == null) return Collections.emptyList();

        return entries.stream()
                .filter(entry -> {
                    Cat cat = entry.getCat();
                    String emsCode = cat.getEmsCode();
                    String[] emsParts = emsCode.split(" ");
                    String breedCode = emsParts[0];
                    String colorCode = emsParts.length > 1 ? emsParts[1] : "";

                    String breedGroupKey = getBreedGroupKey(entry);
                    if (!isQualifiedForBreedGroup(assignment.getJudge(), breedGroupKey)) {
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
}