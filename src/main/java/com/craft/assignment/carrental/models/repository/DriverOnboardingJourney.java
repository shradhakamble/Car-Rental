package com.craft.assignment.carrental.models.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Getter
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
    @Column(name = "created_at")
    private LocalDateTime creationTimestamp;
}
