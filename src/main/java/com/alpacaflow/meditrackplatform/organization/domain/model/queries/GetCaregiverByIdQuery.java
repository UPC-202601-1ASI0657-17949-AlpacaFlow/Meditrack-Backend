package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get a caregiver by ID.
 */
public record GetCaregiverByIdQuery(Long caregiverId) {
    public GetCaregiverByIdQuery {
        if (caregiverId == null || caregiverId <= 0) {
            throw new IllegalArgumentException("caregiverId cannot be null or less than 1");
        }
    }
}

