package com.craft.assignment.carrental.services;

import com.craft.assignment.carrental.enums.DocumentType;
import com.craft.assignment.carrental.enums.JourneyStatus;
import com.craft.assignment.carrental.enums.OnboardingJourneyStep;
import com.craft.assignment.carrental.models.*;
import com.craft.assignment.carrental.repository.DriverOnboardingJourneyHistoryRepository;
import com.craft.assignment.carrental.repository.DriverOnboardingJourneyRepository;
import com.craft.assignment.carrental.repository.DriverRepository;
import com.craft.assignment.carrental.services.external.PartnerDocumentVerificationService;
import com.craft.assignment.carrental.utils.HashUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.JobHoldUntil;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;


@Service
public class DriverOnboardingService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverOnboardingJourneyRepository driverOnboardingJourneyRepository;

    @Autowired
    private DriverOnboardingJourneyHistoryRepository driverOnboardingJourneyHistoryRepository;

    @Autowired
    private PartnerDocumentVerificationService partnerDocumentVerificationService;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public void registerDriver(DriverRegistrationRequest driverRegistrationRequest) throws JsonProcessingException {

        driverRegistrationRequest.setPassword(HashUtils.hashPassword(driverRegistrationRequest.getPassword()));
        driverRepository.saveDriverInfoset(
            objectMapper.writeValueAsString(driverRegistrationRequest.getAddress()),
            driverRegistrationRequest.getContactNumber(),
            driverRegistrationRequest.getDob(),
            driverRegistrationRequest.getEmail(),
            driverRegistrationRequest.getName(),
            driverRegistrationRequest.getPassword(),
            driverRegistrationRequest.getStatus()
        );
    }

    // TODO: this can be made async by pushing to SQS
    /*
        Steps:
        1. Mark current step status as INITIATED & push the journeyId, step & userId to queue
        2. Upload file to s3 & get pre signed url
        3. Pre process the document to verify if details are correct
        4. Mark step as success & store url in DB
     */
    /*
        In current approach: (Sync upload)
        1. Validate the document and save

     */

    public void uploadDocument(Long driverId, OnboardingJourneyStep step, MultipartFile documentFile, DocumentType documentType) throws Exception {
        OnboardingJourneyStep currentStep = step == null ? OnboardingJourneyStep.POI : step;
        // document validated for current step
        try {
            validateDocument(currentStep, documentFile);
        } catch (Exception ex) {
            if(currentStep == OnboardingJourneyStep.POI) {
                driverOnboardingJourneyRepository.saveDriverOnboardingJourney(driverId, currentStep.name(), JourneyStatus.FAILURE.name(), documentType.name());
            }
            else {
                driverOnboardingJourneyRepository.updateJourneyDetailsByDriverId(driverId, currentStep.name(), JourneyStatus.FAILURE.name());
            }
            driverOnboardingJourneyHistoryRepository.saveDriverOnboardingJourneyHistory(driverId, currentStep.name(), JourneyStatus.FAILURE.name(), documentFile.getBytes());
            throw new Exception("Error occurred while verification at step: " + currentStep);
        }
        if(currentStep == OnboardingJourneyStep.POI) {
            driverOnboardingJourneyRepository.saveDriverOnboardingJourney(driverId, currentStep.name(), JourneyStatus.SUCCESS.name(), documentType.name());
        }
        else {
            driverOnboardingJourneyRepository.updateJourneyDetailsByDriverId(driverId, currentStep.name(), JourneyStatus.SUCCESS.name());
        }
        driverOnboardingJourneyHistoryRepository.saveDriverOnboardingJourneyHistory(driverId, currentStep.name(), JourneyStatus.SUCCESS.name(), documentFile.getBytes());
    }

    private void validateDocument(OnboardingJourneyStep step, MultipartFile documentFile) throws Exception {
        boolean response = partnerDocumentVerificationService.validateDocument(step, documentFile);
        if (!response) {
            throw new Exception("Document verification failed for step: " + step.name());
        }
    }

    public void initiateBackgroundVerification(DriverIdentification driverIdentification) {
        // Logic to initiate background verification process
        // ...
    }

    public void shipTrackingDevice(Address address) {
        // Logic to trigger the shipping process for a tracking device
        // ...
    }

    public void markReadyForRide(DriverAvailability driverAvailability) {
        // Logic to update driver's availability status
        // ...
    }

    public OnboardingJourneyStep getCurrentOnboardingStepForAUser(Long driverId) {

        /*
          Step Orders:
          1. POI
          2. DRIVING_LICENSE
          3. VEHICLE_VERIFICATION
          4. PHOTO
         */
        Optional<DriverOnboardingJourney> journey = driverOnboardingJourneyRepository.getJourneyDetailsByDriverId(driverId);
        if (journey.isEmpty() || journey.get().getCurrentStep() == null) {
            return OnboardingJourneyStep.POI;
        }
        else {
            String journeyStatus = journey.get().getCurrentStep();
            if (!Objects.equals(journey.get().getCurrentStepStatus(), JourneyStatus.SUCCESS.name())) {
                return OnboardingJourneyStep.valueOf(journeyStatus);
            }
            if (journey.get().getCurrentStepStatus().equals(JourneyStatus.SUCCESS.name())) {
                if (Objects.equals(journeyStatus, OnboardingJourneyStep.POI.name())) {
                    return OnboardingJourneyStep.DRIVING_LICENSE;
                } else if (Objects.equals(journeyStatus, OnboardingJourneyStep.DRIVING_LICENSE.name())) {
                    return OnboardingJourneyStep.VEHICLE_VERIFICATION;
                } else if (Objects.equals(journeyStatus, OnboardingJourneyStep.PHOTO.name())) {
                    return null;
                }
            }
            else  {
                return OnboardingJourneyStep.valueOf(journeyStatus);
            }
        }
        return null;
    }
}
