package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

/**
 * Update organization resource.
 */
public record UpdateOrganizationResource(
        String name,
        String type
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any of the required fields is null or blank.
     */
    public UpdateOrganizationResource {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Type is required");
        }
        if (!"clinic".equals(type) && !"resident".equals(type)) {
            throw new IllegalArgumentException("Type must be either 'clinic' or 'resident'");
        }
    }
}

