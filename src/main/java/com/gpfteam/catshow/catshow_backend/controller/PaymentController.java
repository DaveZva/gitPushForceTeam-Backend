package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.PaymentIntentResponse;
import com.gpfteam.catshow.catshow_backend.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-intent/{registrationId}")
    public ResponseEntity<PaymentIntentResponse> createPaymentIntent(@PathVariable Long registrationId) {
        try {
            String clientSecret = paymentService.createPaymentIntent(registrationId);
            return ResponseEntity.ok(new PaymentIntentResponse(clientSecret));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}