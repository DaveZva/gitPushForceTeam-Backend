package com.gpfteam.catshow.catshow_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shows") // Název tabulky v databázi

public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob // (Long Object)
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExhibitionStatus status;

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
    private String contactEmail;
    private String websiteUrl;

    public enum ExhibitionStatus {
        PLANNED,  // Připravuje se
        OPEN,     // Otevřeno pro registrace
        CLOSED,   // Registrace uzavřeny
        COMPLETED, // Proběhlo
        CANCELLED  // Zrušeno
    }
}
