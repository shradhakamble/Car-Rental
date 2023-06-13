package com.craft.assignment.carrental.controllers;

import com.craft.assignment.carrental.enums.OnboardingJourneyStep;
import com.craft.assignment.carrental.models.DriverRegistrationRequest;
import com.craft.assignment.carrental.models.LoginRequest;
import com.craft.assignment.carrental.services.impl.AuthService;
import com.craft.assignment.carrental.services.impl.DriverOnboardingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OnboardingControllerTest {
    @Mock
    private AuthService mockAuthService;

    @Mock
    private DriverOnboardingService mockDriverOnboardingService;

    @InjectMocks
    private OnboardingController onboardingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterDriver() throws Exception {
        DriverRegistrationRequest request = new DriverRegistrationRequest();
        request.setEmail("shradha123@gmail.com");

        doNothing().when(mockDriverOnboardingService).registerDriver(any(DriverRegistrationRequest.class));

        ResponseEntity<String> response = onboardingController.registerDriver(request);
        // Verify the mock interactions
        verify(mockDriverOnboardingService).registerDriver(request);

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Driver registered successfully", response.getBody());
    }

    @Test
    public void testLoginDriverValidCredentials() throws Exception {
        // Set up the request body
        LoginRequest request = new LoginRequest();
        request.setEmail("shradha123@gmail.com");
        request.setPassword("password1");

        // Mock the behavior of the authService.authenticateDriver method
        String jwtToken = "token1";
        when(mockAuthService.authenticateDriver(any(String.class), any(String.class), any(String.class))).thenReturn(jwtToken);

        ResponseEntity<?> response = onboardingController.loginDriver("temp", request);
        verify(mockAuthService).authenticateDriver("temp", request.getEmail(), request.getPassword());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUploadDocument() throws Exception {
        // Set up request parameters
        Long driverId = 1L;
        OnboardingJourneyStep currentStep = OnboardingJourneyStep.PHOTO;
        var file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "Test data".getBytes());
        doNothing().when(mockDriverOnboardingService).uploadDocument(driverId, currentStep, file);
        ResponseEntity<String> response = onboardingController.uploadDocuments(driverId, currentStep, file);
        // Verify the mock interactions
        verify(mockDriverOnboardingService).uploadDocument(driverId, currentStep, file);
        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Document uploaded successfully", response.getBody());
    }

    @Test
    public void testMarkDriverActive() throws Exception {
        // Set up request parameters
        Long driverId = 1L;
        // Mock the behavior of the driverOnboardingService.markReadyForRide method
        doNothing().when(mockDriverOnboardingService).markReadyForRide(driverId);

        ResponseEntity<String> response = onboardingController.markDriverActive(driverId);
        // Verify the mock interactions
        verify(mockDriverOnboardingService).markReadyForRide(driverId);
        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Driver marked as active", response.getBody());
    }
}
