package com.craft.assignment.carrental.models;

import com.craft.assignment.carrental.utils.JsonbConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DriverProfile {
    private String name;
    private String contactNumber;
    private String dob;
    private String email;
    private String password;
    private String status;
    private Address address;
}


