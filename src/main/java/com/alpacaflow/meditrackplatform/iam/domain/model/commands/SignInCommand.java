package com.alpacaflow.meditrackplatform.iam.domain.model.commands;

/**
 * Command for signing in a user.
 * Email is used as username for authentication.
 */
public record SignInCommand(String email, String password) {
}

