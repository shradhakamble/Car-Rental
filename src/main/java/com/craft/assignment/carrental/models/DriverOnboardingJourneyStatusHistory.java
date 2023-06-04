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
@Table(name = "driver_onboarding_journey_step_status_history")
public class DriverOnboardingJourneyStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal id;
    private BigDecimal driverId;
    private String step;
    private BigDecimal stepStatus;
    @Column(name = "created_at")
    private LocalDateTime creationTimestamp;
}
