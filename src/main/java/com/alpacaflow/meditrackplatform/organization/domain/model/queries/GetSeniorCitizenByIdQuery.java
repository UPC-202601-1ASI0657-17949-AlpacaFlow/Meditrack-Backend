package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get a senior citizen by ID.
 */
public record GetSeniorCitizenByIdQuery(Long seniorCitizenId) {
    public GetSeniorCitizenByIdQuery {
        if (seniorCitizenId == null || seniorCitizenId <= 0) {
            throw new IllegalArgumentException("seniorCitizenId cannot be null or less than 1");
        }
    }
}

