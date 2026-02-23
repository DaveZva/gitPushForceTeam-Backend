package com.gpfteam.catshow.catshow_backend.model;

import com.gpfteam.catshow.catshow_backend.model.enums.UrgencyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "calling_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long showId;

    private String tableNo;

    private String judgeName;

    private Integer catNumber;

    private String category;

    @Enumerated(EnumType.STRING)
    private UrgencyLevel urgency;

    private LocalDateTime calledAt;
}