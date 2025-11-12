package com.gpfteam.catshow.catshow_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registrations")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String registrationNumber;

    public enum RegistrationStatus {
        PLANNED,  // Čeká na zpracování / platbu
        CONFIRMED, // Potvrzeno, zaplaceno
        REJECTED,  // Zamítnuto
        CANCELLED  // Zrušeno uživatelem
    }

    @Enumerated(EnumType.STRING) // Uloží do DB text ("PLANNED") místo čísla (0)
    @Column(nullable = false)
    private RegistrationStatus status;

    @ManyToOne
    @JoinColumn(name = "exhibition_id", nullable = false)
    private Show exhibition;

    @Column(nullable = false)
    private String days;

    @ManyToOne(cascade = CascadeType.PERSIST) // Uložíme chovatele, pokud je nový
    @JoinColumn(name = "breeder_id", nullable = false)
    private Breeder breeder;

    @ManyToOne(cascade = CascadeType.PERSIST) // Uložíme i vystavovatele
    @JoinColumn(name = "exhibitor_id")
    private Exhibitor exhibitor;

    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL)
    private List<Cat> cats;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private boolean dataAccuracy;
    private boolean gdprConsent;
}