package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

/**
 * Update admin resource.
 */
public record UpdateAdminResource(
        String firstName,
        String lastName
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any of the required fields is null or blank.
     */
    public UpdateAdminResource {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("firstName is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName is required");
        }
    }
}

