package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

import com.alpacaflow.meditrackplatform.organization.domain.model.CaregiverInputRules;

/**
 * Command to create a new caregiver.
 */
public record CreateCaregiverCommand(
        Long organizationId,
        Long userId, // Optional: if null, User will be created automatically
        String firstName,
        String lastName,
        Integer age,
        String email,
        String phoneNumber,
        String imageUrl
) {
    public CreateCaregiverCommand {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 1");
        }
        // userId can be null - if null, it will be created automatically from email
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
        CaregiverInputRules.assertCaregiverAge(age);
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        CaregiverInputRules.assertCaregiverPhoneDigitsOnly(phoneNumber);
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL cannot be null or blank");
        }
    }
}

