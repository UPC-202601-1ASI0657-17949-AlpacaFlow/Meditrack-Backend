package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get a caregiver by user ID.
 */
public record GetCaregiverByUserIdQuery(Long userId) {
    public GetCaregiverByUserIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId cannot be null or less than 1");
        }
    }
}

