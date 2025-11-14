package com.gpfteam.catshow.catshow_backend.dto;
import lombok.Data;

@Data
public class PersonPayload {
    private String firstName;
    private String lastName;
    private String address;
    private String zip;
    private String city;
    private String email;
    private String phone;
    private String ownerLocalOrganization;
    private String ownerMembershipNumber;
}