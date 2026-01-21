package com.gpfteam.catshow.catshow_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registrations")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNumber;

    public enum RegistrationStatus {
        PLANNED,  // Čeká na zpracování / platbu
        CONFIRMED, // Potvrzeno, zaplaceno
        REJECTED,  // Zamítnuto
        CANCELLED  // Zrušeno uživatelem
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(nullable = false)
    private String days;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "breeder_id")
    private Breeder breeder;

    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL)
    private List<RegistrationEntry> entries;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private boolean dataAccuracy;
    private boolean gdprConsent;

    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;

    private Long amountPaid; //halere

    private LocalDateTime paidAt;
}