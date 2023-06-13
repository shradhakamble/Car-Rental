package com.craft.assignment.carrental.services.impl;

import com.craft.assignment.carrental.enums.AccountStatus;
import com.craft.assignment.carrental.enums.JourneyStatus;
import com.craft.assignment.carrental.enums.OnboardingJourneyStep;
import com.craft.assignment.carrental.enums.ShippingStatus;
import com.craft.assignment.carrental.models.*;
import com.craft.assignment.carrental.models.repository.DeviceShippingInfoset;
import com.craft.assignment.carrental.models.repository.DriverOnboardingJourney;
import com.craft.assignment.carrental.repository.DeviceShippingRepository;
import com.craft.assignment.carrental.repository.DriverOnboardingJourneyHistoryRepository;
import com.craft.assignment.carrental.repository.DriverOnboardingJourneyRepository;
import com.craft.assignment.carrental.repository.DriverRepository;
import com.craft.assignment.carrental.services.IDriverOnboardingService;
import com.craft.assignment.carrental.services.external.PartnerDocumentVerificationService;
import com.craft.assignment.carrental.utils.HashUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;


@Service
public class DriverOnboardingService implements IDriverOnboardingService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverOnboardingJourneyRepository driverOnboardingJourneyRepository;

    @Autowired
    private DriverOnboardingJourneyHistoryRepository driverOnboardingJourneyHistoryRepository;

    @Autowired
    private PartnerDocumentVerificationService partnerDocumentVerificationService;

    @Autowired
    private DeviceShippingRepository deviceShippingRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BLR = "BLR";

    Logger logger = LoggerFactory.getLogger(DriverOnboardingService.class);

    public void registerDriver(DriverRegistrationRequest driverRegistrationRequest) throws Exception {
        logger.info("Driver registration request received with email: " + driverRegistrationRequest.getEmail());
        try {
            driverRegistrationRequest.setPassword(HashUtils.hashPassword(driverRegistrationRequest.getPassword()));
            driverRepository.saveDriverInfoset(
                objectMapper.writeValueAsString(driverRegistrationRequest.getAddress()),
                driverRegistrationRequest.getContactNumber(),
                driverRegistrationRequest.getDob(),
                driverRegistrationRequest.getEmail(),
                driverRegistrationRequest.getName(),
                driverRegistrationRequest.getPassword(),
                driverRegistrationRequest.getStatus(),
                driverRegistrationRequest.getVehicleNumber()
            );
            logger.info("Driver registered successfully with email: " + driverRegistrationRequest.getEmail());
        } catch (Exception ex) {
            logger.error("Error occurred while registering driver with email: " + driverRegistrationRequest.getEmail() + "due to: " + ex.getMessage());
            throw ex;
        }
    }

    /*
        Steps:
        1. Mark current step status as INITIATED & push the journeyId, step & userId to queue
        2. Upload file to s3 & get pre signed url
        3. Pre-process the document to verify if details are correct
        4. Mark step as success & store url in DB
     */
    /*
        In current approach: (Sync upload)
        1. Validate the document and save
     */

    public void uploadDocument(Long driverId, OnboardingJourneyStep step, MultipartFile documentFile) throws Exception {
        logger.info("Driver document upload request received for driver :" + driverId + " with current step: " + step.name());

        // document validated for current step
        try {

            validateDocument(step, documentFile);

        } catch (Exception ex) {
            logger.error("Error occurred while uploading document for driver :" + driverId +
                " with current step: " + step.name() + " due to " + ex.getMessage());

            if (step == OnboardingJourneyStep.POI) {
                driverOnboardingJourneyRepository.saveDriverOnboardingJourney(driverId, step.name(), JourneyStatus.FAILURE.name());
            } else {
                driverOnboardingJourneyRepository.updateJourneyDetailsByDriverId(driverId, step.name(), JourneyStatus.FAILURE.name());
            }

            Long journeyId = driverOnboardingJourneyRepository.getJourneyDetailsByDriverId(driverId).get().getId();
            driverOnboardingJourneyHistoryRepository.saveDriverOnboardingJourneyHistory(
                journeyId, driverId, step.name(), JourneyStatus.FAILURE.name(), documentFile.getBytes());

            throw new Exception("Error occurred while verification at step: " + step);
        }

        if (step == OnboardingJourneyStep.POI) {
            driverOnboardingJourneyRepository.saveDriverOnboardingJourney(driverId, step.name(), JourneyStatus.SUCCESS.name());
        } else {
            driverOnboardingJourneyRepository.updateJourneyDetailsByDriverId(driverId, step.name(), JourneyStatus.SUCCESS.name());
        }

        Long journeyId = driverOnboardingJourneyRepository.getJourneyDetailsByDriverId(driverId).get().getId();
        driverOnboardingJourneyHistoryRepository.saveDriverOnboardingJourneyHistory(
            journeyId, driverId, step.name(), JourneyStatus.SUCCESS.name(), documentFile.getBytes());

        if (step == OnboardingJourneyStep.PHOTO) {
            logger.info("Driver document upload request received for driver :" + driverId +
                " with current step: " + step.name() + " completed, initiating device shipping process");

            shipTrackingDevice(driverId);
        }
    }

    void validateDocument(OnboardingJourneyStep step, MultipartFile documentFile) throws Exception {
        boolean response = partnerDocumentVerificationService.validateDocument(step, documentFile);
        if (!response) {
            throw new Exception("Document verification failed for step: " + step.name());
        }
    }


    public void shipTrackingDevice(Long driverId) {
        logger.info("Driver device shipping request for driver :" + driverId + " received");
        deviceShippingRepository.saveDeviceShippingDetails(driverId, ShippingStatus.DISPATCHED.name(), BLR);
    }


    // Will be called based on events triggered by shipping tracking service
    public void markReadyForRide(Long driverId) throws Exception {
        logger.info("Request to mark driver active received for driver :" + driverId);

        DeviceShippingInfoset shippingInfoset = deviceShippingRepository.getShippingDetailsForADriver(driverId).get();

        if (Objects.equals(shippingInfoset.getStatus(), ShippingStatus.DELIVERED.name())) {
            // mark driver as ready
            driverRepository.markDriverAsActive(driverId, AccountStatus.ACTIVE.name());

            logger.info("Driver :" + driverId + " marked active successfully");
        } else {
            throw new Exception("Device is not yet delivered for driver : " + driverId);
        }
    }

    public OnboardingJourneyStep getCurrentOnboardingStepForAUser(Long driverId) throws Exception {

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
        } else {

            String journeyStatus = journey.get().getCurrentStep();

            if (!Objects.equals(journey.get().getCurrentStepStatus(), JourneyStatus.SUCCESS.name())) {
                return OnboardingJourneyStep.valueOf(journeyStatus);
            } else if (journey.get().getCurrentStepStatus().equals(JourneyStatus.SUCCESS.name())) {

                if (Objects.equals(journeyStatus, OnboardingJourneyStep.POI.name())) {
                    return OnboardingJourneyStep.DRIVING_LICENSE;
                } else if (Objects.equals(journeyStatus, OnboardingJourneyStep.DRIVING_LICENSE.name())) {
                    return OnboardingJourneyStep.VEHICLE_VERIFICATION;
                } else if (Objects.equals(journeyStatus, OnboardingJourneyStep.VEHICLE_VERIFICATION.name())) {
                    return OnboardingJourneyStep.PHOTO;
                } else if (Objects.equals(journeyStatus, OnboardingJourneyStep.PHOTO.name())) {
                    return null;
                }
            }
        }

        throw new Exception("Invalid driver information");
    }
}
