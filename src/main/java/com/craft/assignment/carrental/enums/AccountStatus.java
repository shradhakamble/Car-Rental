package com.craft.assignment.carrental.enums;

/**
 * IN_PROGRESS: As onboarding started
 * ACTIVE: ready to accept ride
 * FRAUD: based on background verification can be blocked
 * INACTIVE: Profile deleted
 */
public enum AccountStatus {
    ACTIVE, INACTIVE, FRAUD, IN_PROGRESS
}
