package com.craft.assignment.carrental.repository;

import com.craft.assignment.carrental.models.DriverProfile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<DriverProfile, Long> {

    // Insert Query
//    @Modifying
//    @Query("INSERT INTO DriverProfile (name, contactNumber, vehicleMake, vehicleModel) " +
//        "VALUES (:name, :contactNumber, :vehicleMake, :vehicleModel)")
//    void insertDriver(@Param("name") String name,
//                      @Param("contactNumber") String contactNumber,
//                      @Param("vehicleMake") String vehicleMake,
//                      @Param("vehicleModel") String vehicleModel);

    // Get Query
    @Query("SELECT d FROM DriverProfile d WHERE d.id = :id")
    Optional<DriverProfile> getDriverById(@Param("id") Long id);

    @Query("SELECT d FROM DriverProfile d WHERE d.email = :email")
    Optional<DriverProfile> getDriverByEmail(@Param("email") String email);
}
