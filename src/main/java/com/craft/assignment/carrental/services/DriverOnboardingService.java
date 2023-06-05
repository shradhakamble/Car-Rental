package com.craft.assignment.carrental.services;

import com.craft.assignment.carrental.enums.DocumentType;
import com.craft.assignment.carrental.models.Address;
import com.craft.assignment.carrental.models.DriverAvailability;
import com.craft.assignment.carrental.models.DriverIdentification;
import com.craft.assignment.carrental.models.DriverRegistrationRequest;
import com.craft.assignment.carrental.repository.DriverOnboardingJourneyRepository;
import com.craft.assignment.carrental.repository.DriverRepository;
import com.craft.assignment.carrental.utils.HashUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;


@Service
public class DriverOnboardingService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverOnboardingJourneyRepository driverOnboardingJourneyRepository;
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

    public void uploadDocument(Long driverId, MultipartFile documentFile, DocumentType documentType) throws IOException {
        String currentStep = "";
        if(documentType == DocumentType.POI) {
            currentStep = DocumentType.POI.name();
        }
        else if(documentType == DocumentType.POA) {
            currentStep = DocumentType.POA.name();
        }
        driverOnboardingJourneyRepository.saveDriverOnboardingJourney(driverId, currentStep, "INITIATED", currentStep, documentFile.getBytes());
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
}
