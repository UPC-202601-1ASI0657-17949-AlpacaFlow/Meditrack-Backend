package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to unassign a senior citizen from a caregiver.
 */
public record UnassignSeniorCitizenFromCaregiverCommand(
        Long seniorCitizenId,
        Long caregiverId
) {
    public UnassignSeniorCitizenFromCaregiverCommand {
        if (seniorCitizenId == null || seniorCitizenId <= 0) {
            throw new IllegalArgumentException("seniorCitizenId cannot be null or less than 1");
        }
        if (caregiverId == null || caregiverId <= 0) {
            throw new IllegalArgumentException("caregiverId cannot be null or less than 1");
        }
    }
}

