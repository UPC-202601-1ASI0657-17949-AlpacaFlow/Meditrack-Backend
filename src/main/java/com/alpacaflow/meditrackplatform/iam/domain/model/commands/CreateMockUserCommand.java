package com.alpacaflow.meditrackplatform.iam.domain.model.commands;

/**
 * Command to create a mock user for development purposes.
 * 
 * <p><strong>Note:</strong> This is a temporary command for development/testing.
 * In production, user creation should be handled by the actual IAM bounded context.</p>
 */
public record CreateMockUserCommand(
        String email,
        String role
) {
    public CreateMockUserCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        // Role can be null or blank (optional)
    }
}

