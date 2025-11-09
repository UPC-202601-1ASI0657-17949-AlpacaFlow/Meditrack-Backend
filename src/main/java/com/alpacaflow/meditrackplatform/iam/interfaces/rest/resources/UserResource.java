package com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources;

import java.util.Date;

/**
 * User resource for REST API responses.
 * 
 * <p><strong>Note:</strong> This is a temporary resource for development/testing.
 * In production, user resources should be handled by the actual IAM bounded context.</p>
 */
public record UserResource(
        Long userId,
        String email,
        String role,
        Date createdAt,
        Date updatedAt
) {
}

