package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to delete a senior citizen.
 */
public record DeleteSeniorCitizenCommand(Long seniorCitizenId) {
    public DeleteSeniorCitizenCommand {
        if (seniorCitizenId == null || seniorCitizenId <= 0) {
            throw new IllegalArgumentException("seniorCitizenId cannot be null or less than 1");
        }
    }
}

