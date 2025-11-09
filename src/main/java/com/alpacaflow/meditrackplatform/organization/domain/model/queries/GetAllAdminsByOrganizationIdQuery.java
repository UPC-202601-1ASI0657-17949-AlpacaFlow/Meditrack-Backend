package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get all admins by organization ID.
 */
public record GetAllAdminsByOrganizationIdQuery(Long organizationId) {
    public GetAllAdminsByOrganizationIdQuery {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 1");
        }
    }
}

