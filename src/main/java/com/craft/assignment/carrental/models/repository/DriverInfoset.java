package com.craft.assignment.carrental.models.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "driver_infoset")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverInfoset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigserial")
    private Long id;
    private String name;
    @Column(name = "contact_number")
    private String contactNumber;
    private String dob;
    private String email;
    private String password;
    private String status;
    @Column(name = "created_at")
    private LocalDateTime creationTimestamp;
    @Column(columnDefinition = "jsonb")
    private String address;
    @Column(name = "vehicle_number")
    private String vehicleNumber;
}