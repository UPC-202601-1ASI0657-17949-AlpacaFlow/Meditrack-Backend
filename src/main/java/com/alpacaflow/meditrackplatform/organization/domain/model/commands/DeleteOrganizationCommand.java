package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to delete an organization.
 */
public record DeleteOrganizationCommand(Long organizationId) {
    public DeleteOrganizationCommand {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 1");
        }
    }
}

