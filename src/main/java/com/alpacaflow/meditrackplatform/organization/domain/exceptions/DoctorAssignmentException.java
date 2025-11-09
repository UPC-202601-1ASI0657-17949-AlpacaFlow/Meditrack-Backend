package com.alpacaflow.meditrackplatform.organization.domain.exceptions;

/**
 * Exception thrown when a doctor assignment operation fails due to business rule violations.
 */
public class DoctorAssignmentException extends RuntimeException {
    public DoctorAssignmentException(String message) {
        super(message);
    }
}

