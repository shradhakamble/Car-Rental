package com.craft.assignment.carrental.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {
    private String address;
    private String city;
    private String state;
    private String postalCode;
}
