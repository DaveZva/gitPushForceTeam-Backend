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

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String neutered;

    @Column(nullable = false)
    private String emsCode;

    @Column(nullable = false)
    private String birthDate;

    @Column(nullable = false)
    private String showClass;

    private String pedigreeNumber;

    @Column(nullable = false)
    private String cageType;

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
}