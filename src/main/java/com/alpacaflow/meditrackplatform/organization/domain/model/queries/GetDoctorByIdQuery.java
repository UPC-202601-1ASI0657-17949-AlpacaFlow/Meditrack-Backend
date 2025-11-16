package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get a doctor by ID.
 */
public record GetDoctorByIdQuery(Long doctorId) {
    public GetDoctorByIdQuery {
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("doctorId cannot be null or less than 1");
        }
    }
}

