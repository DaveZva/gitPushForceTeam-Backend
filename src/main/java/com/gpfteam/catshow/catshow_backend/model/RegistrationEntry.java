package com.gpfteam.catshow.catshow_backend.model;

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

    public enum CageType {
        OWN_CAGE, RENT_SMALL, RENT_LARGE
    }

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

    public enum ShowClass {
        SUPREME_CHAMPION("1", 1),
        SUPREME_PREMIOR("2", 2),
        GRANT_INTER_CHAMPION("3", 3),
        GRANT_INTER_PREMIER("4", 4),
        INTERNATIONAL_CHAMPION("5", 5),
        INTERNATIONAL_PREMIER("6", 6),
        CHAMPION("7", 7),
        PREMIER("8", 8),
        OPEN("9", 9),
        NEUTER("10", 10),
        JUNIOR("11", 11),
        KITTEN("12", 12),
        NOVICE_CLASS("13a", 13),
        CONTROL_CLASS("13b", 14),
        DETERMINATION_CLASS("13c", 15),
        DOMESTIC_CAT("14", 16),
        OUT_OF_COMPETITION("15", 17),
        LITTER("16", 18),
        VETERAN("17", 19);

        private final String fifeCode;
        private final int sortOrder;

        ShowClass(String fifeCode, int sortOrder) {
            this.fifeCode = fifeCode;
            this.sortOrder = sortOrder;
        }

        public String getFifeCode() {
            return fifeCode;
        }

        public int getSortOrder() {
            return sortOrder;
        }
    }
}