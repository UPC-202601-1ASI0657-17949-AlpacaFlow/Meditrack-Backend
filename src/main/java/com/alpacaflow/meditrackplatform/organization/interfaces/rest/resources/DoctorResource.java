package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

import java.util.Date;

/**
 * Doctor resource.
 */
public record DoctorResource(
        Long id,
        Long organizationId,
        Long userId,
        String firstName,
        String lastName,
        Integer age,
        String email,
        String specialty,
        String phoneNumber,
        String imageUrl,
        Date createdAt,
        Date updatedAt
) {
}

