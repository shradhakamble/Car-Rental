package com.craft.assignment.carrental.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverRegistrationRequest {
    private String name;
    private String contactNumber;
    private String dob;
    private String email;
    private String password;
    private String status;
    private Address address;
    private String vehicleNumber;
    private String model;
}


