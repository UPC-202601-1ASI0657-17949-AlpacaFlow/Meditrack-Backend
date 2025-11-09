package com.alpacaflow.meditrackplatform.organization.domain.exceptions;

/**
 * Exception thrown when a doctor is not found.
 */
public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(Long doctorId) {
        super("Doctor with ID " + doctorId + " not found.");
    }
}

