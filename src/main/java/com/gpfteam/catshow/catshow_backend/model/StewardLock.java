package com.gpfteam.catshow.catshow_backend.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

@Entity
@Table(name = "steward_locks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StewardLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long showId;
    private Long judgeId;
    private Long userId;
    private String stewardName;
    private Integer tableNumber;
}