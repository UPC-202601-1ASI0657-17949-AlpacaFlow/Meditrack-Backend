package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

import java.util.Date;

/**
 * Update senior citizen resource.
 */
public record UpdateSeniorCitizenResource(
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
    public UpdateSeniorCitizenResource {
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
        if (weight == null || weight <= 0) {
            throw new IllegalArgumentException("Weight is required and must be greater than 0");
        }
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("DNI is required");
        }
        if (height == null || height <= 0) {
            throw new IllegalArgumentException("Height is required and must be greater than 0");
        }
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL is required");
        }
        if (deviceId == null || deviceId <= 0) {
            throw new IllegalArgumentException("Device ID is required and must be greater than 0");
        }
    }
}

