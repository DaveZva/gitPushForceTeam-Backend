package com.gpfteam.catshow.catshow_backend.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EmsUtility {
    private static final Map<String, Integer> BREED_TO_CATEGORY = new HashMap<>();

    static {
        // KATEGORIE 1
        Set.of("EXO", "PER", "RAG", "SBI", "TUV")
                .forEach(b -> BREED_TO_CATEGORY.put(b, 1));

        // KATEGORIE 2
        Set.of("ACL", "ACS", "LPL", "LPS", "MCO", "NEM", "NFO", "SIB", "TUA")
                .forEach(b -> BREED_TO_CATEGORY.put(b, 2));

        // KATEGORIE 3
        Set.of("BEN", "BLH", "BML", "BSH", "BUR", "CYM", "EUR", "CHA", "KBL", "KBS", "KOR", "MAN", "MAU", "OCI", "SIN", "SNO", "SOK", "SRL", "SRS", "BOM")
                .forEach(b -> BREED_TO_CATEGORY.put(b, 3));

        // KATEGORIE 4
        Set.of("ABY", "BAL", "CRX", "DRX", "DSP", "GRX", "JBT", "OLH", "OSH", "PEB", "RUS", "SIA", "SOM", "SPH", "THA", "LYO")
                .forEach(b -> BREED_TO_CATEGORY.put(b, 4));

        // KATEGORIE 5 (Domácí kočky)
        Set.of("HCL", "HCS")
                .forEach(b -> BREED_TO_CATEGORY.put(b, 5));
    }

    public static int getCategory(String emsCode) {
        if (emsCode == null || emsCode.isEmpty()) {
            return 99;
        }

        String breed = getBreedFromEms(emsCode);
        return BREED_TO_CATEGORY.getOrDefault(breed, 99);
    }

    public static String getBreedFromEms(String emsCode) {
        if (emsCode == null || emsCode.isBlank()) {
            return "UNKNOWN";
        }
        return emsCode.trim().toUpperCase().split("\\s+")[0];
    }
}