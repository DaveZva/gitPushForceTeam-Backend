package com.gpfteam.catshow.catshow_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String zip;
    private String city;
    @Column(unique = true)
    private String email;
    private String phone;
    private String ownerLocalOrganization;
    private String ownerMembershipNumber;
}