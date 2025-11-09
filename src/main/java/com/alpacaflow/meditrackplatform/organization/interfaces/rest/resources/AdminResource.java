package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

import java.util.Date;

/**
 * Admin resource.
 */
public record AdminResource(
        Long id,
        Long organizationId,
        Long userId,
        String firstName,
        String lastName,
        Date createdAt,
        Date updatedAt
) {
}

