package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to create a new organization.
 */
public record CreateOrganizationCommand(
        String name,
        String type
) {
    public CreateOrganizationCommand {
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

