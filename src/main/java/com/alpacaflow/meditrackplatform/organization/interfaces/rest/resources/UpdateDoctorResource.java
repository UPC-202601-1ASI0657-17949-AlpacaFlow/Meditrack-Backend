package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

/**
 * Update doctor resource.
 */
public record UpdateDoctorResource(
        String firstName,
        String lastName,
        Integer age,
        String email,
        String specialty,
        String phoneNumber,
        String imageUrl
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any of the required fields is null or blank.
     */
    public UpdateDoctorResource {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (age == null || age <= 0) {
            throw new IllegalArgumentException("Age is required and must be greater than 0");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (specialty == null || specialty.isBlank()) {
            throw new IllegalArgumentException("Specialty is required");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL is required");
        }
    }
}

