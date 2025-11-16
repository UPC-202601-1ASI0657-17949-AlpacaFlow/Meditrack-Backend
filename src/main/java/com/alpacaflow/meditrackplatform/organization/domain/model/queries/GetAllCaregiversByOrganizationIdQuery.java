package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get all caregivers by organization ID.
 */
public record GetAllCaregiversByOrganizationIdQuery(Long organizationId) {
    public GetAllCaregiversByOrganizationIdQuery {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 1");
        }
    }
}

