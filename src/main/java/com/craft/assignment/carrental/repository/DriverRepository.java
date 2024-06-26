package com.craft.assignment.carrental.repository;

import com.craft.assignment.carrental.models.repository.DriverInfoset;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface DriverRepository extends JpaRepository<DriverInfoset, Long> {

    @Query("SELECT d FROM DriverInfoset d WHERE d.email = :email")
    Optional<DriverInfoset> getDriverByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "INSERT INTO driver_infoset (address, contact_number, dob, email, name, password, status, vehicle_number) " +
        "VALUES (CAST(:address AS jsonb), :contactNumber, :dob, :email, :name, :password, :status, :vehicleNumber)",
        nativeQuery = true)
    void saveDriverInfoset(@Param("address") String address,
                           @Param("contactNumber") String contactNumber,
                           @Param("dob") String dob,
                           @Param("email") String email,
                           @Param("name") String name,
                           @Param("password") String password,
                           @Param("status") String status,
                           @Param("vehicleNumber") String vehicleNumber);


    @Modifying
    @Query("UPDATE DriverInfoset d SET d.status = :status WHERE d.id = :driverId")
    void markDriverAsActive(@Param("driverId") Long driverId, @Param("status") String status);

}
