package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

import com.alpacaflow.meditrackplatform.organization.domain.validation.SeniorCitizenPersonalDataValidation;

import java.util.Date;

/**
 * Create senior citizen resource.
 */
public record CreateSeniorCitizenResource(
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
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any of the required fields is null or blank.
     */
    public CreateSeniorCitizenResource {
        // Allow organizationId = 0 as special case for relatives (individual users)
        if (organizationId == null || organizationId < 0) {
            throw new IllegalArgumentException("Organization ID is required and must be greater than or equal to 0");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date is required");
        }
        if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("Gender is required");
        }
        if (weight == null) {
            throw new IllegalArgumentException("Weight is required");
        }
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("DNI is required");
        }
        if (height == null) {
            throw new IllegalArgumentException("Height is required");
        }
        SeniorCitizenPersonalDataValidation.validatePersonalData(birthDate, gender, weight, height, dni);
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL is required");
        }
        if (deviceId == null || deviceId <= 0) {
            throw new IllegalArgumentException("Device ID is required and must be greater than 0");
        }
    }
}

