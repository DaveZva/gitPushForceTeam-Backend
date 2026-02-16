package com.gpfteam.catshow.catshow_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "judge_assignments")
public class JudgeAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @ManyToOne
    @JoinColumn(name = "judge_id", nullable = false)
    private Judge judge;

    @Column(nullable = false)
    private String day;

    @ElementCollection
    @CollectionTable(name = "judge_assignment_breeds", joinColumns = @JoinColumn(name = "assignment_id"))
    @Column(name = "breed_code")
    @Builder.Default
    private List<String> assignedBreedCodes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "judge_assignment_colors", joinColumns = @JoinColumn(name = "assignment_id"))
    @Column(name = "color_code")
    @Builder.Default
    private List<String> assignedColorCodes = new ArrayList<>();
}