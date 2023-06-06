package com.craft.assignment.carrental.controllers;

import com.craft.assignment.carrental.enums.DocumentType;
import com.craft.assignment.carrental.enums.JourneyStatus;
import com.craft.assignment.carrental.enums.OnboardingJourneyStep;
import com.craft.assignment.carrental.models.*;
import com.craft.assignment.carrental.services.AuthService;
import com.craft.assignment.carrental.services.DriverOnboardingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/driver")
@RestController
public class OnboardingController {

    private final DriverOnboardingService driverOnboardingService;

    private final AuthService authService;

    public OnboardingController(DriverOnboardingService driverOnboardingService, AuthService authService) {
        this.driverOnboardingService = driverOnboardingService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerDriver(@RequestBody DriverRegistrationRequest driverRegistrationRequest) throws JsonProcessingException {
        driverOnboardingService.registerDriver(driverRegistrationRequest);
        return new ResponseEntity<>("Driver registered successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginDriver(@RequestBody LoginRequest loginRequest) {
        String jwtToken = authService.authenticateDriver(loginRequest.getEmail(), loginRequest.getPassword());

        if (jwtToken != null) {
            return ResponseEntity.ok(new LoginResponse(jwtToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }


    @PostMapping("/registered/documents/upload")
    public ResponseEntity<String> uploadDocuments(@RequestParam("driverId") Long driverId,
                                                  @RequestParam("type") DocumentType type,
                                                  @RequestParam(value = "currentStep", required = false) OnboardingJourneyStep currentStep,
                                                  @RequestParam(value = "file", required = false) MultipartFile file
                                                 ) throws Exception {

        driverOnboardingService.uploadDocument(driverId, currentStep, file, type);
        return new ResponseEntity<>("Document uploaded successfully", HttpStatus.OK);
    }

    @PostMapping("/registered/background-verification")
    public ResponseEntity<String> initiateBackgroundVerification(@RequestBody DriverIdentification driverIdentification) {
        return new ResponseEntity<>("Background verification initiated", HttpStatus.OK);
    }

    @GetMapping("/registered/onboarding/current-step")
    public ResponseEntity<OnboardingJourneyStep> getCurrentOnboardingStepForAUser(@RequestParam("driverId") Long driverId) {
        OnboardingJourneyStep status = driverOnboardingService.getCurrentOnboardingStepForAUser(driverId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping("/registered/tracking-device/shipping")
    public ResponseEntity<String> shipTrackingDevice(@RequestBody Address address) {
        // Logic to trigger the shipping process for a tracking device
        // ...

        return new ResponseEntity<>("Tracking device shipping process initiated", HttpStatus.OK);
    }

    @PostMapping("/registered/ride/ready")
    public ResponseEntity<String> markReadyForRide(@RequestBody DriverAvailability driverAvailability) {
        // Logic to update driver's availability status
        // ...

        return new ResponseEntity<>("Driver marked as ready for a ride", HttpStatus.OK);
    }
}
