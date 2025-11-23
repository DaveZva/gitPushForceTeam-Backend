package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.model.Registration;
import com.gpfteam.catshow.catshow_backend.repository.RegistrationRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class PaymentService {

    private final RegistrationRepository registrationRepository;

    @Value("${spring.stripe.api.key}")
    private String stripeApiKey;

    public PaymentService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public String createPaymentIntent(Long registrationId) throws StripeException {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registrace nenalezena"));

        long price = calculatePrice(registration);

        if (price <= 0) {
            throw new RuntimeException("Cena musí být vyšší než 0");
        }

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(price)
                .setCurrency("czk")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .putMetadata("registration_id", registrationId.toString())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return paymentIntent.getClientSecret();
    }

    // TODO: Implementovat reálnou logiku ceníku podle zadání (klecné + startovné)
    private long calculatePrice(Registration registration) {
        long basePrice = 50000; // 500 CZK základ
        long catPrice = 30000;  // 300 CZK za kočku

        int catCount = registration.getCats() != null ? registration.getCats().size() : 0;
        return basePrice + (catPrice * catCount);
    }
}