package com.alpacaflow.meditrackplatform.iam.domain.model.commands;

/**
 * Command for signing up a new user.
 * Email is used as username for authentication.
 * If role is 'admin', organization and admin details must be provided.
 */
public record SignUpCommand(
        String email, 
        String password, 
        String role,
        String firstName,
        String lastName,
        String organizationName,
        String organizationType
) {
    /**
     * Constructor for sign up without role (backward compatibility)
     */
    public SignUpCommand(String email, String password) {
        this(email, password, null, null, null, null, null);
    }
    
    /**
     * Constructor for sign up with role only (backward compatibility)
     */
    public SignUpCommand(String email, String password, String role) {
        this(email, password, role, null, null, null, null);
    }
}

