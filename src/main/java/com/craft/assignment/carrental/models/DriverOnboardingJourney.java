package com.craft.assignment.carrental.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
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
    @Column(columnDefinition = "bigserial")
    private Long id;
    @Column(columnDefinition = "bigserial")
    private Long driverId;
    private String currentStep;
    private String currentStepStatus;
    private String documentName;
    @Lob
    @Column(name = "document", columnDefinition = "bytea")
    @Transient
    private Blob document;
    @Column(name = "created_at")
    private LocalDateTime creationTimestamp;
}
