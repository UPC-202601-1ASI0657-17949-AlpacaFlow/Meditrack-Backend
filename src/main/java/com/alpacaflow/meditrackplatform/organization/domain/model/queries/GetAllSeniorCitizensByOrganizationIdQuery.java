package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get all senior citizens by organization ID.
 */
public record GetAllSeniorCitizensByOrganizationIdQuery(Long organizationId) {
    public GetAllSeniorCitizensByOrganizationIdQuery {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 1");
        }
    }
}

