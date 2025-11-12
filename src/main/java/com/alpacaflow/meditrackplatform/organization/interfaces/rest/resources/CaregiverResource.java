package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

import java.util.Date;

/**
 * Caregiver resource.
 */
public record CaregiverResource(
        Long id,
        Long organizationId,
        Long userId,
        String firstName,
        String lastName,
        Integer age,
        String email,
        String phoneNumber,
        String imageUrl,
        Date createdAt,
        Date updatedAt
) {
}

