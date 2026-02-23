package com.gpfteam.catshow.catshow_backend.model;

import com.gpfteam.catshow.catshow_backend.model.enums.CageType;
import com.gpfteam.catshow.catshow_backend.model.enums.ShowClass;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @Enumerated(EnumType.STRING)
    private ShowClass showClass;


    @Enumerated(EnumType.STRING)
    private CageType cageType;

    private boolean neutered;

    private Integer catalogNumber;

    @JsonProperty("showClassCode")
    public String getShowClassCode() {
        if (showClass == null) {
            return null;
        }
        return showClass.getFifeCode();
    }

    @JsonProperty("showClassSortOrder")
    public Integer getShowClassSortOrder() {
        if (showClass == null) {
            return null;
        }
        return showClass.getSortOrder();
    }
}