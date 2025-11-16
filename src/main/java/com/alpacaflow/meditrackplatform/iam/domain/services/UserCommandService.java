package com.alpacaflow.meditrackplatform.iam.domain.services;

import com.alpacaflow.meditrackplatform.iam.domain.model.aggregates.User;
import com.alpacaflow.meditrackplatform.iam.domain.model.commands.CreateMockUserCommand;
import com.alpacaflow.meditrackplatform.iam.domain.model.commands.SignInCommand;
import com.alpacaflow.meditrackplatform.iam.domain.model.commands.SignUpCommand;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

/**
 * UserCommandService
 * Service that handles user commands including authentication
 */
public interface UserCommandService {
    /**
     * Handle a sign-in command
     * @param command The sign-in command containing email and password
     * @return An Optional with a pair of User and JWT token if authentication succeeds
     */
    Optional<ImmutablePair<User, String>> handle(SignInCommand command);

    /**
     * Handle a sign-up command
     * @param command The sign-up command containing email, password and optional role
     * @return An Optional with the created user
     */
    Optional<User> handle(SignUpCommand command);

    /**
     * Handle a create mock user command (for development/testing)
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

