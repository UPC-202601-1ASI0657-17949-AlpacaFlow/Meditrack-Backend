package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

import com.alpacaflow.meditrackplatform.organization.domain.validation.SeniorCitizenPersonalDataValidation;

import java.util.Date;

/**
 * Command to create a new senior citizen.
 */
public record CreateSeniorCitizenCommand(
        Long organizationId,
        String firstName,
        String lastName,
        Date birthDate,
        String gender,
        Double weight,
        String dni,
        Double height,
        String imageUrl,
        Long deviceId
) {
    public CreateSeniorCitizenCommand {
        // Allow organizationId = 0 as special case for relatives (individual users)
        if (organizationId == null || organizationId < 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 0");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("Gender cannot be null or blank");
        }
        if (weight == null) {
            throw new IllegalArgumentException("Weight cannot be null");
        }
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("DNI cannot be null or blank");
        }
        if (height == null) {
            throw new IllegalArgumentException("Height cannot be null");
        }
        SeniorCitizenPersonalDataValidation.validatePersonalData(birthDate, gender, weight, height, dni);
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL cannot be null or blank");
        }
        // deviceId can be null (will auto-create device) or must be > 0 if provided
        if (deviceId != null && deviceId <= 0) {
            throw new IllegalArgumentException("Device ID must be greater than 0 if provided");
        }
    }
}

