package com.craft.assignment.carrental.models.repository;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "driver_onboarding_journey")
public class DriverOnboardingJourney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigserial")
    private Long id;
    @Column(columnDefinition = "bigserial")
    private Long driverId;
    @Column(columnDefinition = "jsonb")
    private String metadata;
    private String currentStep;
    private String currentStepStatus;
    @Column(name = "created_at")
    private LocalDateTime creationTimestamp;
}
