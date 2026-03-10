package com.gpfteam.catshow.catshow_backend.model;

import com.gpfteam.catshow.catshow_backend.model.enums.RegistrationStatus;
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
@Table(name = "registrations", indexes = {
        @Index(name = "idx_reg_show_status", columnList = "show_id, status"),
        @Index(name = "idx_reg_user", columnList = "user_id")
})
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNumber;


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

    @PostPersist
    public void generateRegistrationNumber() {
        if (this.registrationNumber == null || this.registrationNumber.startsWith("PENDING-")) {this.registrationNumber = "REG-" + java.time.Year.now().getValue() + "-" + this.id;
        }
    }

}