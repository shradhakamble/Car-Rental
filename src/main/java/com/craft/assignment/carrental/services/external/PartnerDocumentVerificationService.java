package com.craft.assignment.carrental.services.external;

import com.craft.assignment.carrental.enums.OnboardingJourneyStep;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class will interact external partner API integration to do the document validation or any in house
 * service available to do the same.
 *
 * On production env: Partner layer implementation can be done in a common sdk that can be added as dependency
 * in our service.
 */
@Service
public class PartnerDocumentVerificationService {

   // Mock function for now
    public boolean validateDocument(OnboardingJourneyStep step, MultipartFile documentFile) {
        return true;
    }
}
