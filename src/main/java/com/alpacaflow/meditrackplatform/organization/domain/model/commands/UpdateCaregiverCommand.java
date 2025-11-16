package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to update an existing caregiver.
 */
public record UpdateCaregiverCommand(
        Long caregiverId,
        String firstName,
        String lastName,
        Integer age,
        String email,
        String phoneNumber,
        String imageUrl
) {
    public UpdateCaregiverCommand {
        if (caregiverId == null || caregiverId <= 0) {
            throw new IllegalArgumentException("caregiverId cannot be null or less than 1");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
    }
}

