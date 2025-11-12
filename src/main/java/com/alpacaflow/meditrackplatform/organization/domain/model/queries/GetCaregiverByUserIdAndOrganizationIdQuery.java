package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get a caregiver by user ID and organization ID.
 */
public record GetCaregiverByUserIdAndOrganizationIdQuery(Long userId, Long organizationId) {
    public GetCaregiverByUserIdAndOrganizationIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId cannot be null or less than 1");
        }
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 1");
        }
    }
}

