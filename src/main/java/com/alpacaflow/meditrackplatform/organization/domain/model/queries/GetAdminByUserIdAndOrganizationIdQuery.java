package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get an admin by user ID and organization ID.
 */
public record GetAdminByUserIdAndOrganizationIdQuery(Long userId, Long organizationId) {
    public GetAdminByUserIdAndOrganizationIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId cannot be null or less than 1");
        }
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 1");
        }
    }
}

