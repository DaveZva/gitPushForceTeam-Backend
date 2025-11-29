package com.gpfteam.catshow.catshow_backend.controller;

import com.gpfteam.catshow.catshow_backend.dto.PaymentIntentResponse;
import com.gpfteam.catshow.catshow_backend.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    @Value("${STRIPE_WEBHOOK_SECRET}")
    private String endpointSecret;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-intent/{registrationId}")
    public ResponseEntity<?> createPaymentIntent(@PathVariable Long registrationId) {
        try {
            String clientSecret = paymentService.createPaymentIntent(registrationId);
            return ResponseEntity.ok(new PaymentIntentResponse(clientSecret));

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Chyba platební brány: " + e.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        System.out.println("====== WEBHOOK ZAVOLÁN ======");
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("1. Podpis ověřen OK.");
            System.out.println("2. Typ eventu: " + event.getType());
        } catch (StripeException e) {
            System.out.println("!!! CHYBA PODPISU !!!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chyba podpisu webhooku");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            System.out.println("3. Typ eventu je správný (payment_intent.succeeded).");

            var deserializer = event.getDataObjectDeserializer();
            PaymentIntent paymentIntent = (PaymentIntent) deserializer.getObject().orElse(null);

            if (paymentIntent == null && deserializer.getRawJson() != null) {
                try {
                    System.out.println("WARN: Automatická deserializace selhala. Zkouším manuální pars...");
                    paymentIntent = com.stripe.net.ApiResource.GSON.fromJson(deserializer.getRawJson(), PaymentIntent.class);
                    System.out.println("WARN: Manuální pars úspěšný! ID: " + paymentIntent.getId());
                } catch (Exception e) {
                    System.out.println("ERR: Ani manuální pars nevyšel: " + e.getMessage());
                }
            }

            if (paymentIntent != null) {
                System.out.println("4. Deserializace OK. ID platby: " + paymentIntent.getId());
                System.out.println("5. Volám paymentService...");
                try {
                    paymentService.handleWebhookPaymentSuccess(paymentIntent);
                    System.out.println("6. paymentService doběhla v pořádku.");
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                System.out.println("!!! CHYBA: PaymentIntent je NULL (Deserializace selhala) !!!");
                System.out.println("Raw JSON objektu: " + deserializer.getRawJson());
            }
        } else {
            System.out.println("INFO: Ignoruji event typu: " + event.getType());
        }

        System.out.println("====== KONEC WEBHOOKU (vracím 200) ======");
        return ResponseEntity.ok("Zpracováno");
    }
}