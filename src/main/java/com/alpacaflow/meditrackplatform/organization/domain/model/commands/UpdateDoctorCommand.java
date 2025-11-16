package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to update an existing doctor.
 */
public record UpdateDoctorCommand(
        Long doctorId,
        String firstName,
        String lastName,
        String specialty,
        Integer age,
        String email,
        String phoneNumber,
        String imageUrl
) {
    public UpdateDoctorCommand {
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("doctorId cannot be null or less than 1");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
        if (specialty == null || specialty.isBlank()) {
            throw new IllegalArgumentException("Specialty cannot be null or blank");
        }
    }
}

