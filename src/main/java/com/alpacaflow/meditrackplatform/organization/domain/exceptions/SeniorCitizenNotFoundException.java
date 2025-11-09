package com.alpacaflow.meditrackplatform.organization.domain.exceptions;

/**
 * Exception thrown when a senior citizen is not found.
 */
public class SeniorCitizenNotFoundException extends RuntimeException {
    public SeniorCitizenNotFoundException(Long seniorCitizenId) {
        super("Senior citizen with ID " + seniorCitizenId + " not found.");
    }
}

