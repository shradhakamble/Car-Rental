package com.craft.assignment.carrental.repository;

import com.craft.assignment.carrental.models.repository.DriverOnboardingJourneyStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface DriverOnboardingJourneyHistoryRepository extends JpaRepository<DriverOnboardingJourneyStatusHistory, Long> {
    @Modifying
    @Query(value = "INSERT INTO driver_onboarding_journey_step_status_history (driver_id, step, step_status," +
        "document) " +
        "VALUES (:driverId, :step, :stepStatus, :document)",
        nativeQuery = true)
    void saveDriverOnboardingJourneyHistory(@Param("driverId") Long driverId,
                                     @Param("step") String step,
                                     @Param("stepStatus") String stepStatus,
                                     @Param("document") byte[] document);

    @Query(value = "SELECT id, driver_id, current_step, step, step_status, created_at " +
        "FROM driver_onboarding_journey_step_status_history " +
        "WHERE driver_id = :driverId",
        nativeQuery = true)
    Optional<DriverOnboardingJourneyStatusHistory> getJourneyHistoryByDriverId(@Param("driverId") Long driverId);

}
