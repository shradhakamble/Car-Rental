package com.craft.assignment.carrental.controllers;

import com.craft.assignment.carrental.enums.OnboardingJourneyStep;
import com.craft.assignment.carrental.models.*;
import com.craft.assignment.carrental.services.impl.AuthService;
import com.craft.assignment.carrental.services.impl.DriverOnboardingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/onboarding/driver")
@RestController
public class OnboardingController {

    private final DriverOnboardingService driverOnboardingService;
    private final AuthService authService;

    public OnboardingController(DriverOnboardingService driverOnboardingService, AuthService authService) {
        this.driverOnboardingService = driverOnboardingService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerDriver(@RequestBody DriverRegistrationRequest driverRegistrationRequest) throws Exception {
        driverOnboardingService.registerDriver(driverRegistrationRequest);
        return new ResponseEntity<>("Driver registered successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginDriver(@RequestHeader("Session-Id") String sessionId, @RequestBody LoginRequest loginRequest) {
        String jwtToken = authService.authenticateDriver(sessionId, loginRequest.getEmail(), loginRequest.getPassword());

        if (jwtToken != null) {
            return ResponseEntity.ok(new LoginResponse(jwtToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }


    @PostMapping("/auth/document-upload")
    public ResponseEntity<String> uploadDocuments(@RequestParam("driverId") Long driverId,
                                                  @RequestParam(value = "currentStep", required = false) OnboardingJourneyStep currentStep,
                                                  @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {

        driverOnboardingService.uploadDocument(driverId, currentStep, file);
        return new ResponseEntity<>("Document uploaded successfully", HttpStatus.OK);
    }

    @GetMapping("/auth/current-step")
    public ResponseEntity<OnboardingJourneyStep> getCurrentOnboardingStepForAUser(@RequestParam("driverId") Long driverId) throws Exception {
        OnboardingJourneyStep status = driverOnboardingService.getCurrentOnboardingStepForAUser(driverId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping("/auth/mark-driver-active")
    public ResponseEntity<String> markDriverActive(@RequestParam("driverId") Long driverId
    ) throws Exception {
        driverOnboardingService.markReadyForRide(driverId);
        return new ResponseEntity<>("Driver marked as active", HttpStatus.OK);
    }

}
