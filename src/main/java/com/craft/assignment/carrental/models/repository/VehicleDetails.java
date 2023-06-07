package com.craft.assignment.carrental.models.repository;

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
@Table(name = "driver_vehicle_infoset")
public class VehicleDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal id;
    private String vehicleNumber;
    private BigDecimal driverId;
    private String registeredName;
    private String model;
    @Column(name = "created_at")
    private LocalDateTime registrationTimestamp;
}
