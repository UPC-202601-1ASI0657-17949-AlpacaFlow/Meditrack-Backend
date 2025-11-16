package com.alpacaflow.meditrackplatform.organization.domain.exceptions;

/**
 * Exception thrown when a senior citizen assignment operation fails due to business rule violations.
 */
public class SeniorCitizenAssignmentException extends RuntimeException {
    public SeniorCitizenAssignmentException(String message) {
        super(message);
    }
}

