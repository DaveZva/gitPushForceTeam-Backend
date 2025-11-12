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
@Table(name = "cats")
public class Cat {

    public enum Gender {
        MALE, FEMALE
    }

    public enum Neutered {
        YES, NO
    }

    // Názvy musí přesně odpovídat tomu, co posílá frontend, ale VELKÝMI PÍSMENY
    public enum CageType {
        OWN_CAGE, RENT_SMALL, RENT_LARGE
    }

    // Tohle je dlouhé, ale je to tak správně
    public enum ShowClass {
        SUPREME_CHAMPION, SUPREME_PREMIOR, GRANT_INTER_CHAMPION, GRANT_INTER_PREMIER,
        INTERNATIONAL_CHAMPION, INTERNATIONAL_PREMIER, CHAMPION, PREMIER,
        OPEN, NEUTER, JUNIOR, KITTEN, NOVICE_CLASS, CONTROL_CLASS,
        DETERMINATION_CLASS, DOMESTIC_CAT, OUT_OF_COMPETITION, LITTER, VETERAN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false)
    private Registration registration;

    private String titleBefore;

    @Column(nullable = false)
    private String catName;

    private String titleAfter;
    private String chipNumber;

    // ZMĚNIT:
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    // ZMĚNIT:
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Neutered neutered;

    @Column(nullable = false)
    private String emsCode; // EMS kód zůstává String

    @Column(nullable = false)
    private String birthDate; // Datum zůstává String

    // ZMĚNIT:
    @Enumerated(EnumType.STRING)
    @Column(name = "show_class", nullable = false) // 'show_class' je pravděpodobný název sloupce v DB
    private ShowClass showClass;

    private String pedigreeNumber; // Zůstává String

    // ZMĚNIT:
    @Enumerated(EnumType.STRING)
    @Column(name = "cage_type", nullable = false) // 'cage_type' je pravděpodobný název sloupce v DB
    private CageType cageType;

    private String motherTitleBefore;
    private String motherName;
    private String motherTitleAfter;
    private String motherBreed;
    private String motherEmsCode;
    private String motherColor;
    private String motherPedigreeNumber;

    private String fatherTitleBefore;
    private String fatherName;
    private String fatherTitleAfter;
    private String fatherBreed;
    private String fatherEmsCode;
    private String fatherColor;
    private String fatherPedigreeNumber;

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }
}