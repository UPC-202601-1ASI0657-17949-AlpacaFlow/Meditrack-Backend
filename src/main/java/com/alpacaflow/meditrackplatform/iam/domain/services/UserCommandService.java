package com.alpacaflow.meditrackplatform.iam.domain.services;

import com.alpacaflow.meditrackplatform.iam.domain.model.aggregates.User;
import com.alpacaflow.meditrackplatform.iam.domain.model.commands.CreateMockUserCommand;

import java.util.Optional;

/**
 * UserCommandService
 * Service that handles user commands
 * 
 * <p><strong>Note:</strong> This is a temporary service for development/testing.
 * In production, user commands should be handled by the actual IAM bounded context.</p>
 */
public interface UserCommandService {
    /**
     * Handle a create mock user command
     * @param command The create mock user command containing the user data
     * @return The created user
     * @see CreateMockUserCommand
     */
    User handle(CreateMockUserCommand command);

    /**
     * Get a user by ID
     * @param userId The user ID
     * @return An Optional with the user if found, otherwise an empty Optional
     */
    Optional<User> getUserById(Long userId);

    /**
     * Get a user by email
     * @param email The user's email address
     * @return An Optional with the user if found, otherwise an empty Optional
     */
    Optional<User> getUserByEmail(String email);
}

