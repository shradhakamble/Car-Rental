package com.craft.assignment.carrental.services;

import com.craft.assignment.carrental.enums.DocumentType;
import com.craft.assignment.carrental.models.Address;
import com.craft.assignment.carrental.models.DriverAvailability;
import com.craft.assignment.carrental.models.DriverIdentification;
import com.craft.assignment.carrental.models.DriverProfile;
import com.craft.assignment.carrental.repository.DriverRepository;
import com.craft.assignment.carrental.utils.HashUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class DriverOnboardingService {

    @Autowired
    private DriverRepository driverRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public void registerDriver(DriverProfile driverProfile) throws JsonProcessingException {
        driverProfile.setPassword(HashUtils.hashPassword(driverProfile.getPassword()));
        driverRepository.saveDriverInfoset(objectMapper.writeValueAsString(driverProfile.getAddress()), driverProfile.getContactNumber(),
            driverProfile.getDob(), driverProfile.getEmail(), driverProfile.getName(), driverProfile.getPassword(), driverProfile.getStatus());
    }

    public void uploadDocument(MultipartFile documentFile, DocumentType documentType) {
        // Logic to save the uploaded document to the database or file storage
        // ...
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
