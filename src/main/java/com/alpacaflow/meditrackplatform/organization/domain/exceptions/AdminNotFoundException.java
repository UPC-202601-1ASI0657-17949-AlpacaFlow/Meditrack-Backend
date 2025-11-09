package com.alpacaflow.meditrackplatform.organization.domain.exceptions;

/**
 * Exception thrown when an admin is not found.
 */
public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(Long adminId) {
        super("Admin with ID " + adminId + " not found.");
    }
}

