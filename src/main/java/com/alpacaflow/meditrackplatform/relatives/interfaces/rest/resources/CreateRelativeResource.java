package com.alpacaflow.meditrackplatform.relatives.interfaces.rest.resources;

/**
 * Create relative resource.
 */
public record CreateRelativeResource(
        Long userId, // Optional: can be null
        String firstName,
        String lastName,
        String phoneNumber,
        String planType, // "FREEMIUM" or "PREMIUM"
        Long seniorCitizenId
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any of the required fields is null or blank.
     */
    public CreateRelativeResource {
        // userId can be null - optional
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (planType == null || planType.isBlank()) {
            throw new IllegalArgumentException("Plan type is required");
        }
        if (seniorCitizenId == null || seniorCitizenId <= 0) {
            throw new IllegalArgumentException("Senior citizen ID is required and must be greater than 0");
        }
    }
}

