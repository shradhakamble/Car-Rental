package com.craft.assignment.carrental.repository;

import com.craft.assignment.carrental.models.repository.DeviceShippingInfoset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface DeviceShippingRepository extends JpaRepository<DeviceShippingInfoset, Long> {


    @Query("SELECT d FROM DeviceShippingInfoset d WHERE d.driverId = :driverId")
    Optional<DeviceShippingInfoset> getShippingDetailsForADriver(@Param("driverId") Long driverId);

    @Modifying
    @Query(value = "INSERT INTO device_shipping_infoset (driver_id, status, current_location) " +
        "VALUES ( :driverId, :status, :location)",
        nativeQuery = true)
    void saveDeviceShippingDetails(@Param("driverId") Long driverId,
                                   @Param("status") String status,
                                   @Param("location") String location);


}