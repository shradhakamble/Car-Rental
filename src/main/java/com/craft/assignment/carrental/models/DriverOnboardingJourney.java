package com.craft.assignment.carrental.models;

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
@Table(name = "driver_onboarding_journey")
public class DriverOnboardingJourney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal id;
    private BigDecimal driverId;
    private String currentStep;
    private BigDecimal currentStepStatus;
    private String documentName;
    @Lob
    private byte[] document;
    @Column(name = "created_at")
    private LocalDateTime creationTimestamp;
}
