package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class RegistrationPayload {
    private ShowData show;
    private List<CatPayload> cats;
    private PersonPayload owner;
    private PersonPayload breeder;
    private String notes;
    private Map<String, Boolean> consents;

    @Data
    public static class ShowData {
        private String id;
        private String days;
    }
}