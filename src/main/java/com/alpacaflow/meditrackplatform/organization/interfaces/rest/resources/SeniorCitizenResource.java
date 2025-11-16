package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

import java.util.Date;

/**
 * Senior citizen resource.
 */
public record SeniorCitizenResource(
        Long id,
        Long organizationId,
        String firstName,
        String lastName,
        Date birthDate,
        Integer age,
        String gender,
        Double weight,
        String dni,
        Double height,
        String imageUrl,
        Long deviceId,
        Long assignedDoctorId,
        Long assignedCaregiverId,
        Date createdAt,
        Date updatedAt
) {
}

