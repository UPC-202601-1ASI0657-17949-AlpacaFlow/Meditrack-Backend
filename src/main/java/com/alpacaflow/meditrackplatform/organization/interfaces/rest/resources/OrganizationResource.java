package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

import java.util.Date;

/**
 * Organization resource.
 */
public record OrganizationResource(
        Long id,
        String name,
        String type,
        Date createdAt,
        Date updatedAt
) {
}

