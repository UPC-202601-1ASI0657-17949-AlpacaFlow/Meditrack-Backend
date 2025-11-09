package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get an organization by ID.
 */
public record GetOrganizationByIdQuery(Long organizationId) {
    public GetOrganizationByIdQuery {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 1");
        }
    }
}

