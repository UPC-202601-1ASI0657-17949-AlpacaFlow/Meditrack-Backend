package com.alpacaflow.meditrackplatform.organization.domain.exceptions;

/**
 * Exception thrown when a caregiver is not found.
 */
public class CaregiverNotFoundException extends RuntimeException {
    public CaregiverNotFoundException(Long caregiverId) {
        super("Caregiver with ID " + caregiverId + " not found.");
    }
}

