package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to update an existing organization.
 */
public record UpdateOrganizationCommand(
        Long organizationId,
        String name,
        String type
) {
    public UpdateOrganizationCommand {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 1");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Type cannot be null or blank");
        }
        if (!"clinic".equals(type) && !"resident".equals(type)) {
            throw new IllegalArgumentException("Type must be either 'clinic' or 'resident'");
        }
    }
}

