package com.craft.assignment.carrental.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    private String current_location;
    @Column(name = "created_at")
    private LocalDateTime creationTimestamp;
}