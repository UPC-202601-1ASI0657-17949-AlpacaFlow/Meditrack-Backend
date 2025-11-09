package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to delete an existing admin.
 */
public record DeleteAdminCommand(Long adminId) {
    public DeleteAdminCommand {
        if (adminId == null || adminId <= 0) {
            throw new IllegalArgumentException("adminId cannot be null or less than 1");
        }
    }
}

