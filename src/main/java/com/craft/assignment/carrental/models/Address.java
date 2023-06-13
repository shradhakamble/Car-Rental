package com.craft.assignment.carrental.models;

import jakarta.persistence.*;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class Address {
    @Column(name = "address_line1")
    private String addressLine1;
    @Column(name = "address_line2")
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    @Column(name = "postal_code")
    private String postalCode;
}