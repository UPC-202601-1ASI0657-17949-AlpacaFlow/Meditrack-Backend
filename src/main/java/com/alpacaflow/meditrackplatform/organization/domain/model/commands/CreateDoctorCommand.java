package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to create a new doctor.
 */
public record CreateDoctorCommand(
        Long organizationId,
        Long userId, // Optional: if null, User will be created automatically
        String firstName,
        String lastName,
        String specialty,
        Integer age,
        String email,
        String phoneNumber,
        String imageUrl
) {
    public CreateDoctorCommand {
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
        if (age == null || age <= 0) {
            throw new IllegalArgumentException("Age cannot be null or less than 1");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (specialty == null || specialty.isBlank()) {
            throw new IllegalArgumentException("Specialty cannot be null or blank");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL cannot be null or blank");
        }
    }
}

