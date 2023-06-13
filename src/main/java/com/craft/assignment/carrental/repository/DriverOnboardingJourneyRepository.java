package com.craft.assignment.carrental.repository;

import com.craft.assignment.carrental.models.repository.DriverOnboardingJourney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface DriverOnboardingJourneyRepository extends JpaRepository<DriverOnboardingJourney, Long> {
    @Modifying
    @Query(value = "INSERT INTO driver_onboarding_journey (driver_id, current_step, current_step_status)" +
        "VALUES (:driverId, :currentStep, :currentStepStatus)",
        nativeQuery = true)
    int saveDriverOnboardingJourney(@Param("driverId") Long driverId,
                                    @Param("currentStep") String currentStep,
                                    @Param("currentStepStatus") String currentStepStatus);


    @Query(value = "SELECT id, driver_id, current_step, current_step_status, metadata, created_at " +
        "FROM driver_onboarding_journey WHERE driver_id = :driverId",
        nativeQuery = true)
    Optional<DriverOnboardingJourney> getJourneyDetailsByDriverId(@Param("driverId") Long driverId);


    @Modifying
    @Query("UPDATE DriverOnboardingJourney d SET d.currentStep = :currentStep, d.currentStepStatus = :currentStepStatus WHERE d.driverId = :driverId")
    void updateJourneyDetailsByDriverId(@Param("driverId") Long driverId, @Param("currentStep") String currentStep, @Param("currentStepStatus") String currentStepStatus);


}
