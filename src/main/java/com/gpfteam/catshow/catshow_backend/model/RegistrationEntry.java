package com.gpfteam.catshow.catshow_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registration_entries")
public class RegistrationEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false)
    private Registration registration;

    @ManyToOne
    private Cat cat;

    @Enumerated(EnumType.STRING)
    private ShowClass showClass;

    @Enumerated(EnumType.STRING)
    private CageType cageType;

    private boolean neutered;

    private Integer catalogNumber;

    public enum CageType {
        OWN_CAGE, RENT_SMALL, RENT_LARGE
    }

    public enum ShowClass {
        SUPREME_CHAMPION, SUPREME_PREMIOR, GRANT_INTER_CHAMPION, GRANT_INTER_PREMIER,
        INTERNATIONAL_CHAMPION, INTERNATIONAL_PREMIER, CHAMPION, PREMIER,
        OPEN, NEUTER, JUNIOR, KITTEN, NOVICE_CLASS, CONTROL_CLASS,
        DETERMINATION_CLASS, DOMESTIC_CAT, OUT_OF_COMPETITION, LITTER, VETERAN
    }
}