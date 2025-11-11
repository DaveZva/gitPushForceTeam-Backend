package com.gpfteam.catshow.catshow_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "exhibition_id", nullable = false)
    private Exhibition exhibition;

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

    private boolean dataAccuracy;
    private boolean gdprConsent;
}