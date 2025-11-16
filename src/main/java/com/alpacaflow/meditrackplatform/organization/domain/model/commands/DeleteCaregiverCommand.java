package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to delete a caregiver.
 */
public record DeleteCaregiverCommand(Long caregiverId) {
    public DeleteCaregiverCommand {
        if (caregiverId == null || caregiverId <= 0) {
            throw new IllegalArgumentException("caregiverId cannot be null or less than 1");
        }
    }
}

