package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to create a new admin.
 */
public record CreateAdminCommand(
        Long organizationId,
        Long userId,
        String firstName,
        String lastName
) {
    public CreateAdminCommand {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 1");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId cannot be null or less than 1");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("firstName cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName cannot be null or blank");
        }
    }
}

