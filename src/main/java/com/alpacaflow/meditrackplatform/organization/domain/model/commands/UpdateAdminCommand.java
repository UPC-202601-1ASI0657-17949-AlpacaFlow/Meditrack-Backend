package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to update an existing admin.
 */
public record UpdateAdminCommand(
        Long adminId,
        String firstName,
        String lastName
) {
    public UpdateAdminCommand {
        if (adminId == null || adminId <= 0) {
            throw new IllegalArgumentException("adminId cannot be null or less than 1");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("firstName cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName cannot be null or blank");
        }
    }
}

