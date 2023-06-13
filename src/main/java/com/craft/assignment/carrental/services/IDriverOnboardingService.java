package com.craft.assignment.carrental.services;

import com.craft.assignment.carrental.enums.OnboardingJourneyStep;
import com.craft.assignment.carrental.models.DriverRegistrationRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IDriverOnboardingService {
    void registerDriver(DriverRegistrationRequest driverRegistrationRequest) throws Exception;
    void uploadDocument(Long driverId, OnboardingJourneyStep step, MultipartFile documentFile) throws Exception;
    void markReadyForRide(Long driverId) throws Exception;
    OnboardingJourneyStep getCurrentOnboardingStepForAUser(Long driverId) throws Exception;
}
