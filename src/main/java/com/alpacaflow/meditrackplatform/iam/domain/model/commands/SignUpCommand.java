package com.alpacaflow.meditrackplatform.iam.domain.model.commands;

/**
 * Command for signing up a new user.
 * Email is used as username for authentication.
 */
public record SignUpCommand(String email, String password, String role) {
    /**
     * Constructor for sign up without role
     */
    public SignUpCommand(String email, String password) {
        this(email, password, null);
    }
}

