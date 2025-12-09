package com.gpfteam.catshow.catshow_backend.util;

import java.util.Set;

public class EmsUtility {
    private static final Set<String> CAT_1 = Set.of("EXO", "PER", "RAG", "SBI", "TUV");
    private static final Set<String> CAT_2 = Set.of("ACL", "ACS", "LPL", "LPS", "MCO", "NEM", "NFO", "SIB", "TUA");
    private static final Set<String> CAT_3 = Set.of("BEN", "BLH", "BML", "BSH", "BUR", "CHA", "CYM", "EUR", "KBL", "KBS", "KOR", "MAN", "MAU", "OCI", "SIN", "SNO", "SOK", "SRL", "SRS");

    public static int getCategory(String emsCode) {
        if (emsCode == null || emsCode.isEmpty()) return 99;

        String code = emsCode.toUpperCase().split(" ")[0];

        if (CAT_1.contains(code)) return 1;
        if (CAT_2.contains(code)) return 2;
        if (CAT_3.contains(code)) return 3;
        if (code.startsWith("HCS") || code.startsWith("HCL")) return 5;

        return 4;
    }
}
