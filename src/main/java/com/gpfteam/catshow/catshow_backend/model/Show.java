package com.gpfteam.catshow.catshow_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shows")

public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShowStatus status;

    @Column(nullable = false)
    private String venueName;
    private String venueAddress;
    private String venueCity;
    private String venueState;
    private String venueZip;

    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @Column(nullable = false)
    private LocalDateTime registrationDeadline;

    @Column(nullable = false)
    private String organizerName;
    private String organizerContactEmail;
    private String organizerWebsiteUrl;

    @Column(nullable = false)
    private Integer maxCats;

    private LocalDateTime vetCheckStart;
    private LocalDateTime judgingStart;
    private LocalDateTime judgingEnd;

    public enum ShowStatus {
        PLANNED,  // Připravuje se
        OPEN,     // Otevřeno pro registrace
        CLOSED,   // Registrace uzavřeny
        COMPLETED, // Proběhlo
        CANCELLED  // Zrušeno
    }

    @ManyToMany
    @JoinTable(
            name = "show_judges",
            joinColumns = @JoinColumn(name = "show_id"),
            inverseJoinColumns = @JoinColumn(name = "judge_id")
    )
    @Builder.Default
    private List<Judge> judges = new ArrayList<>();

    @OneToMany(mappedBy = "show", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Registration> registrations;
}
