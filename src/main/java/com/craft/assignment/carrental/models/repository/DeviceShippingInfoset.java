package com.craft.assignment.carrental.models.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Setter
@Table(name = "device_shipping_infoset")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceShippingInfoset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigserial")
    private Long id;
    @Column(columnDefinition = "bigserial")
    private Long driverId;
    private String status;
    @Column(name = "current_location")
    private String currentLocation;
    @Column(name = "created_at")
    private LocalDateTime creationTimestamp;
}