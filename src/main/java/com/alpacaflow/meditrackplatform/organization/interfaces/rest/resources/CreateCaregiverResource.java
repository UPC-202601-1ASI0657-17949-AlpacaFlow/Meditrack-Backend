package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

import com.alpacaflow.meditrackplatform.organization.domain.model.CaregiverInputRules;

/**
 * Create caregiver resource.
 */
public record CreateCaregiverResource(
        Long organizationId,
        Long userId, // Optional: if null, User will be created automatically from email
        String firstName,
        String lastName,
        Integer age,
        String email,
        String phoneNumber,
        String imageUrl
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any of the required fields is null or blank.
     */
    public CreateCaregiverResource {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("Organization ID is required and must be greater than 0");
        }
        // userId is optional - if null, it will be created automatically
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        CaregiverInputRules.assertCaregiverAge(age);
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        CaregiverInputRules.assertCaregiverPhoneDigitsOnly(phoneNumber);
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL is required");
        }
    }
}

