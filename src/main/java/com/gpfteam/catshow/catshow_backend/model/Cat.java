package com.gpfteam.catshow.catshow_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cats")
@Data
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User ownerUser;

    private String titleBefore;
    private String catName;
    private String titleAfter;
    private String emsCode;
    private String birthDate;
    private String pedigreeNumber;
    private String chipNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String fatherTitleBefore;
    private String fatherName;
    private String fatherTitleAfter;
    private String fatherBirthDate;
    private String fatherEmsCode;
    private String fatherPedigreeNumber;
    private String fatherChipNumber;

    private String motherTitleBefore;
    private String motherName;
    private String motherTitleAfter;
    private String motherBirthDate;
    private String motherEmsCode;
    private String motherPedigreeNumber;
    private String motherChipNumber;

    public enum Gender {
        MALE, FEMALE
    }
}