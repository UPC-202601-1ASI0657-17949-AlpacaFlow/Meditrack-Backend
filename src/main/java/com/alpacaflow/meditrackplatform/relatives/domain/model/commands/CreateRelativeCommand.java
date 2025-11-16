package com.alpacaflow.meditrackplatform.relatives.domain.model.commands;

/**
 * Command to create a new relative.
 */
public record CreateRelativeCommand(
        Long userId,
        String firstName,
        String lastName,
        String phoneNumber,
        String planType, // "FREEMIUM" or "PREMIUM"
        Long seniorCitizenId
) {
    public CreateRelativeCommand {
        // userId can be null - optional
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }
        if (planType == null || planType.isBlank()) {
            throw new IllegalArgumentException("Plan type cannot be null or blank");
        }
        if (seniorCitizenId == null || seniorCitizenId <= 0) {
            throw new IllegalArgumentException("Senior citizen ID cannot be null or less than 1");
        }
    }
}

