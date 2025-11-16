package com.alpacaflow.meditrackplatform.organization.domain.exceptions;

/**
 * Exception thrown when an organization is not found.
 */
public class OrganizationNotFoundException extends RuntimeException {
    public OrganizationNotFoundException(Long organizationId) {
        super("Organization with ID " + organizationId + " not found.");
    }
}

