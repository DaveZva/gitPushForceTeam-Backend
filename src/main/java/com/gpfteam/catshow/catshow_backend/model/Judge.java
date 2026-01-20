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
@Table(name = "judges")
public class Judge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String country;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "judge_valid_groups", joinColumns = @JoinColumn(name = "judge_id"))
    @Column(name = "group_code")
    @Builder.Default
    private List<String> validGroups = new ArrayList<>();
}