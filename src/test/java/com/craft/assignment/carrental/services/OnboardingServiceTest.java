package com.craft.assignment.carrental.services;

import com.craft.assignment.carrental.enums.AccountStatus;
import com.craft.assignment.carrental.enums.JourneyStatus;
import com.craft.assignment.carrental.enums.OnboardingJourneyStep;
import com.craft.assignment.carrental.enums.ShippingStatus;
import com.craft.assignment.carrental.models.Address;
import com.craft.assignment.carrental.models.DriverRegistrationRequest;
import com.craft.assignment.carrental.models.repository.DeviceShippingInfoset;
import com.craft.assignment.carrental.models.repository.DriverOnboardingJourney;
import com.craft.assignment.carrental.repository.DeviceShippingRepository;
import com.craft.assignment.carrental.repository.DriverOnboardingJourneyHistoryRepository;
import com.craft.assignment.carrental.repository.DriverOnboardingJourneyRepository;
import com.craft.assignment.carrental.repository.DriverRepository;
import com.craft.assignment.carrental.services.external.PartnerDocumentVerificationService;
import com.craft.assignment.carrental.services.impl.DriverOnboardingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OnboardingServiceTest {

    @Mock
    private DriverRepository mockDriverRepository;

    @Mock
    private DriverOnboardingJourneyRepository mockDriverOnboardingJourneyRepository;

    @Mock
    private DriverOnboardingJourneyHistoryRepository mockDriverOnboardingJourneyHistoryRepository;

    @Mock
    private PartnerDocumentVerificationService mockPartnerDocumentVerificationService;

    @Mock
    private DeviceShippingRepository mockDeviceShippingRepository;

    @InjectMocks
    DriverOnboardingService onboardingService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterDriver() throws Exception {
        DriverRegistrationRequest driverRegistrationRequest = new DriverRegistrationRequest("Shraddha",
            "918707042343", "24-04-1996", "shradh123@gmail.com", "password1", "IN_PROGRESS",
            new Address("H N 12", "Pune tower 3", "Pune", "Maharashtra", "560034", "INDIA"), "KA-01-2343",
            "XUV");
        onboardingService.registerDriver(driverRegistrationRequest);

        // Verify that the driverRepository.saveDriverInfoset method is called with the expected arguments
        verify(mockDriverRepository).saveDriverInfoset(
            anyString(),
            anyString(),
            anyString(),
            anyString(),
            anyString(),
            anyString(),
            anyString(),
            anyString()
        );
    }

    @Test
    public void testUploadDocumentForPOI() throws Exception {
        // Create an instance of the MultipartFile with a mock file content
        MockMultipartFile mockMultipartFile = new MockMultipartFile("document", "document.txt", "text/plain", "Mock document content".getBytes());

        // Set up the arguments for the uploadDocument method
        Long driverId = 1L;
        OnboardingJourneyStep step = OnboardingJourneyStep.POI;
        when(mockPartnerDocumentVerificationService.validateDocument(step, mockMultipartFile)).thenReturn(true);
        DriverOnboardingJourney mockedDriverJourneyInfoset = new DriverOnboardingJourney();
        mockedDriverJourneyInfoset.setDriverId(driverId);
        mockedDriverJourneyInfoset.setId(1L);
        when(mockDriverOnboardingJourneyRepository.getJourneyDetailsByDriverId(driverId)).thenReturn(Optional.of(mockedDriverJourneyInfoset));
        // Call the method to be tested
        onboardingService.uploadDocument(driverId, step, mockMultipartFile);

        // Verify that the mocked dependencies are interacted with as expected
        verify(mockDriverOnboardingJourneyRepository).saveDriverOnboardingJourney(driverId, step.name(), JourneyStatus.SUCCESS.name());
        verify(mockDriverOnboardingJourneyHistoryRepository).saveDriverOnboardingJourneyHistory(1L, driverId, step.name(), JourneyStatus.SUCCESS.name(), mockMultipartFile.getBytes());
    }

    @Test
    public void testUploadDocumentForLicense() throws Exception {
        // Create an instance of the MultipartFile with a mock file content
        MockMultipartFile mockMultipartFile = new MockMultipartFile("document", "document.txt", "text/plain", "Mock document content".getBytes());

        // Set up the arguments for the uploadDocument method
        Long driverId = 1L;
        OnboardingJourneyStep step = OnboardingJourneyStep.DRIVING_LICENSE;
        when(mockPartnerDocumentVerificationService.validateDocument(step, mockMultipartFile)).thenReturn(true);
        // Call the method to be tested

        DriverOnboardingJourney mockedDriverJourneyInfoset = new DriverOnboardingJourney();
        mockedDriverJourneyInfoset.setDriverId(driverId);
        mockedDriverJourneyInfoset.setId(1L);
        when(mockDriverOnboardingJourneyRepository.getJourneyDetailsByDriverId(driverId)).thenReturn(Optional.of(mockedDriverJourneyInfoset));
        onboardingService.uploadDocument(driverId, step, mockMultipartFile);

        // Verify that the mocked dependencies are interacted with as expected
        verify(mockDriverOnboardingJourneyRepository, times(2)).updateJourneyDetailsByDriverId(driverId, step.name(), JourneyStatus.SUCCESS.name());
        verify(mockDriverOnboardingJourneyHistoryRepository).saveDriverOnboardingJourneyHistory(1L, driverId, step.name(), JourneyStatus.SUCCESS.name(), mockMultipartFile.getBytes());
    }

    @Test
    public void testUploadDocumentForVehicleReg() throws Exception {
        // Create an instance of the MultipartFile with a mock file content
        MockMultipartFile mockMultipartFile = new MockMultipartFile("document", "document.txt", "text/plain", "Mock document content".getBytes());

        // Set up the arguments for the uploadDocument method
        Long driverId = 1L;
        OnboardingJourneyStep step = OnboardingJourneyStep.VEHICLE_VERIFICATION;
        when(mockPartnerDocumentVerificationService.validateDocument(step, mockMultipartFile)).thenReturn(true);
        // Call the method to be tested
        DriverOnboardingJourney mockedDriverJourneyInfoset = new DriverOnboardingJourney();
        mockedDriverJourneyInfoset.setDriverId(driverId);
        mockedDriverJourneyInfoset.setId(1L);
        when(mockDriverOnboardingJourneyRepository.getJourneyDetailsByDriverId(driverId)).thenReturn(Optional.of(mockedDriverJourneyInfoset));
        onboardingService.uploadDocument(driverId, step, mockMultipartFile);

        // Verify that the mocked dependencies are interacted with as expected
        verify(mockDriverOnboardingJourneyRepository, times(2)).updateJourneyDetailsByDriverId(driverId, step.name(), JourneyStatus.SUCCESS.name());
        verify(mockDriverOnboardingJourneyHistoryRepository).saveDriverOnboardingJourneyHistory(1L, driverId, step.name(), JourneyStatus.SUCCESS.name(), mockMultipartFile.getBytes());
    }

    @Test
    public void testUploadDocumentForVehiclePhoto() throws Exception {
        // Create an instance of the MultipartFile with a mock file content
        MockMultipartFile mockMultipartFile = new MockMultipartFile("document", "document.txt", "text/plain", "Mock document content".getBytes());

        // Set up the arguments for the uploadDocument method
        Long driverId = 1L;
        OnboardingJourneyStep step = OnboardingJourneyStep.PHOTO;
        when(mockPartnerDocumentVerificationService.validateDocument(step, mockMultipartFile)).thenReturn(true);
        DriverOnboardingJourney mockedDriverJourneyInfoset = new DriverOnboardingJourney();
        mockedDriverJourneyInfoset.setDriverId(driverId);
        mockedDriverJourneyInfoset.setId(1L);
        when(mockDriverOnboardingJourneyRepository.getJourneyDetailsByDriverId(driverId)).thenReturn(Optional.of(mockedDriverJourneyInfoset));
        // Call the method to be tested
        onboardingService.uploadDocument(driverId, step, mockMultipartFile);

        // Verify that the mocked dependencies are interacted with as expected
        verify(mockDriverOnboardingJourneyRepository, times(2)).updateJourneyDetailsByDriverId(driverId, step.name(), JourneyStatus.SUCCESS.name());
        verify(mockDriverOnboardingJourneyHistoryRepository).saveDriverOnboardingJourneyHistory(1L, driverId, step.name(), JourneyStatus.SUCCESS.name(), mockMultipartFile.getBytes());
        verify(mockDeviceShippingRepository).saveDeviceShippingDetails(driverId, ShippingStatus.DISPATCHED.name(), "BLR");
    }

    @Test
    public void testMarkReadyForRide() throws Exception {
        Long driverId = 1L;
        DeviceShippingInfoset mockedShippingInfoset = new DeviceShippingInfoset();
        mockedShippingInfoset.setStatus(ShippingStatus.DELIVERED.name());

        when(mockDeviceShippingRepository.getShippingDetailsForADriver(driverId)).thenReturn(Optional.of(mockedShippingInfoset));
        onboardingService.markReadyForRide(driverId);

        // Verify that the mocked dependencies are interacted with as expected
        verify(mockDeviceShippingRepository).getShippingDetailsForADriver(driverId);
        verify(mockDriverRepository).markDriverAsActive(driverId, AccountStatus.ACTIVE.name());
    }

    @Test
    public void testGetCurrentOnboardingStepForAUserForPhotoStep() throws Exception {
        Long driverId = 1L;

        DriverOnboardingJourney mockedJourney = new DriverOnboardingJourney();
        mockedJourney.setCurrentStep(OnboardingJourneyStep.PHOTO.name());
        mockedJourney.setCurrentStepStatus(JourneyStatus.SUCCESS.name());

        when(mockDriverOnboardingJourneyRepository.getJourneyDetailsByDriverId(driverId)).thenReturn(Optional.of(mockedJourney));

        // Call the method to be tested
        OnboardingJourneyStep result = onboardingService.getCurrentOnboardingStepForAUser(driverId);

        // Verify that the mocked dependencies are interacted with as expected
        verify(mockDriverOnboardingJourneyRepository).getJourneyDetailsByDriverId(driverId);
        assertNull(result);
    }

    @Test
    public void testGetCurrentOnboardingStepForAUserForPOIStep() throws Exception {
        Long driverId = 1L;

        DriverOnboardingJourney mockedJourney = new DriverOnboardingJourney();
        mockedJourney.setCurrentStep(OnboardingJourneyStep.POI.name());
        mockedJourney.setCurrentStepStatus(JourneyStatus.SUCCESS.name());

        when(mockDriverOnboardingJourneyRepository.getJourneyDetailsByDriverId(driverId)).thenReturn(Optional.of(mockedJourney));

        // Call the method to be tested
        OnboardingJourneyStep result = onboardingService.getCurrentOnboardingStepForAUser(driverId);

        // Verify that the mocked dependencies are interacted with as expected
        verify(mockDriverOnboardingJourneyRepository).getJourneyDetailsByDriverId(driverId);
        assertEquals(OnboardingJourneyStep.DRIVING_LICENSE, result);
    }

    @Test
    public void testGetCurrentOnboardingStepForAUserForDLStep() throws Exception {
        Long driverId = 1L;

        DriverOnboardingJourney mockedJourney = new DriverOnboardingJourney();
        mockedJourney.setCurrentStep(OnboardingJourneyStep.DRIVING_LICENSE.name());
        mockedJourney.setCurrentStepStatus(JourneyStatus.SUCCESS.name());

        when(mockDriverOnboardingJourneyRepository.getJourneyDetailsByDriverId(driverId)).thenReturn(Optional.of(mockedJourney));

        // Call the method to be tested
        OnboardingJourneyStep result = onboardingService.getCurrentOnboardingStepForAUser(driverId);

        // Verify that the mocked dependencies are interacted with as expected
        verify(mockDriverOnboardingJourneyRepository).getJourneyDetailsByDriverId(driverId);
        assertEquals(OnboardingJourneyStep.VEHICLE_VERIFICATION, result);
    }

    @Test
    public void testGetCurrentOnboardingStepForAUserForVRStep() throws Exception {
        Long driverId = 1L;

        DriverOnboardingJourney mockedJourney = new DriverOnboardingJourney();
        mockedJourney.setCurrentStep(OnboardingJourneyStep.VEHICLE_VERIFICATION.name());
        mockedJourney.setCurrentStepStatus(JourneyStatus.SUCCESS.name());

        when(mockDriverOnboardingJourneyRepository.getJourneyDetailsByDriverId(driverId)).thenReturn(Optional.of(mockedJourney));

        // Call the method to be tested
        OnboardingJourneyStep result = onboardingService.getCurrentOnboardingStepForAUser(driverId);

        // Verify that the mocked dependencies are interacted with as expected
        verify(mockDriverOnboardingJourneyRepository).getJourneyDetailsByDriverId(driverId);
        assertEquals(OnboardingJourneyStep.PHOTO, result);
    }
}
