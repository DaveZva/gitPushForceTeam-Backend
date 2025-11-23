package com.gpfteam.catshow.catshow_backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PaymentIntentResponse {
    private String clientSecret;

    public PaymentIntentResponse(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}