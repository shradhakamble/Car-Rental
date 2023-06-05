package com.craft.assignment.carrental.repository;

import com.craft.assignment.carrental.models.DriverInfoset;
import com.craft.assignment.carrental.models.DriverOnboardingJourney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DriverOnboardingJourneyRepository extends JpaRepository<DriverOnboardingJourney, Long>  {
    @Modifying
    @Query(value = "INSERT INTO driver_onboarding_journey (driver_id, current_step, current_step_status," +
        " document_name, document) " +
        "VALUES (:driverId, :currentStep, :currentStepStatus, :documentName, :document)",
        nativeQuery = true)
    void saveDriverOnboardingJourney(@Param("driverId") Long driverId,
                                     @Param("currentStep") String currentStep,
                                     @Param("currentStepStatus") String currentStepStatus,
                                     @Param("documentName") String documentName,
                                     @Param("document") byte[] document);
}
