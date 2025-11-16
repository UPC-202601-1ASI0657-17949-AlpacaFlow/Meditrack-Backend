package com.alpacaflow.meditrackplatform.organization.domain.model.queries;

/**
 * Query to get an admin by its ID.
 */
public record GetAdminByIdQuery(Long adminId) {
    public GetAdminByIdQuery {
        if (adminId == null || adminId <= 0) {
            throw new IllegalArgumentException("adminId cannot be null or less than 1");
        }
    }
}

