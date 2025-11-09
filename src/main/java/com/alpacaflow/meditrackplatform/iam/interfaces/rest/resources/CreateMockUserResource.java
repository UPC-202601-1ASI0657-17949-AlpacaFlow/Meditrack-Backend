package com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources;

/**
 * Create mock user resource.
 * 
 * <p><strong>Note:</strong> This is a temporary resource for development/testing.
 * In production, user creation should be handled by the actual IAM bounded context.</p>
 */
public record CreateMockUserResource(
        String email,
        String role
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if email is null or blank.
     */
    public CreateMockUserResource {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email is required");
        }
        // Role is optional, can be null or blank
    }
}

