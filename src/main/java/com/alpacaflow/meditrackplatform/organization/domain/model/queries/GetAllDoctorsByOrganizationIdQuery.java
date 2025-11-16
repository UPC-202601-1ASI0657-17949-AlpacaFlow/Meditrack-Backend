package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get all doctors by organization ID.
 */
public record GetAllDoctorsByOrganizationIdQuery(Long organizationId) {
    public GetAllDoctorsByOrganizationIdQuery {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId cannot be null or less than 1");
        }
    }
}

