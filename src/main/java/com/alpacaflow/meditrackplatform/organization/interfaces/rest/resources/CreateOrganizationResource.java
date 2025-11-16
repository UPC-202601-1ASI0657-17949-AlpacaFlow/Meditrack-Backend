package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

/**
 * Create organization resource.
 */
public record CreateOrganizationResource(
        String name,
        String type
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any of the required fields is null or blank.
     */
    public CreateOrganizationResource {
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

