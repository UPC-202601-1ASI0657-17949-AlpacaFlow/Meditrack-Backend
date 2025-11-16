package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

/**
 * Create admin resource.
 */
public record CreateAdminResource(
        Long organizationId,
        Long userId,
        String firstName,
        String lastName
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any of the required fields is null or blank.
     */
    public CreateAdminResource {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId is required and must be greater than 0");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId is required and must be greater than 0");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("firstName is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName is required");
        }
    }
}

