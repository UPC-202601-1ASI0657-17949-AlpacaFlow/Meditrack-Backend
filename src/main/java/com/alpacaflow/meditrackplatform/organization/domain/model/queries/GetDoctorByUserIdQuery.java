package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get a doctor by user ID.
 */
public record GetDoctorByUserIdQuery(Long userId) {
    public GetDoctorByUserIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId cannot be null or less than 1");
        }
    }
}

