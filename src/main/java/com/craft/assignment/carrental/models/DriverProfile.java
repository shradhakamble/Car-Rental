package com.craft.assignment.carrental.models;

import com.craft.assignment.carrental.enums.AccountStatus;
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
@Entity
@Table(name = "driver_infoset")
public class DriverProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal id;
    private String name;
    @Column(name = "contact_number")
    private String contactNumber;
    private String dob;
    private String email;
    private String password;
    private AccountStatus status;
    @Column(name = "created_at")
    private LocalDateTime creationTimestamp;
    @Column(columnDefinition = "jsonb")
    private Address address;
}
