package com.gpfteam.catshow.catshow_backend.model;

import com.gpfteam.catshow.catshow_backend.model.enums.JudgingStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "judging_sheets", indexes = {
        @Index(name = "idx_sheet_show_judge_cat", columnList = "show_id, judge_id, day"),
        @Index(name = "idx_sheet_show_day", columnList = "show_id, day")
})
public class JudgingSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @ManyToOne
    @JoinColumn(name = "judge_id", nullable = false)
    private Judge judge;

    @ManyToOne
    @JoinColumn(name = "cat_entry_id", nullable = false)
    private RegistrationEntry catEntry;

    @Column(nullable = false)
    private String day;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JudgingStatus status;

    private Integer catalogNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String grade;
    private String notes;
    private Boolean nominated;
}