package com.alpacaflow.meditrackplatform.iam.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.iam.domain.model.aggregates.User;
import com.alpacaflow.meditrackplatform.iam.domain.model.commands.CreateMockUserCommand;
import com.alpacaflow.meditrackplatform.iam.domain.services.UserCommandService;
import com.alpacaflow.meditrackplatform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the UserCommandService interface.
 * <p>This class is responsible for handling the commands related to the User aggregate.</p>
 * 
 * <p><strong>Note:</strong> This is a temporary implementation for development/testing.
 * In production, user commands should be handled by the actual IAM bounded context.</p>
 * 
 * @see UserCommandService
 * @see UserRepository
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;

    /**
     * Constructor of the class.
     * @param userRepository the repository to be used by the class.
     */
    public UserCommandServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handle a create mock user command
     * @param command The create mock user command containing the user data
     * @return The created user
     * @throws IllegalStateException if a user with the given email already exists
     */
    @Override
    public User handle(CreateMockUserCommand command) {
        // Check if user with this email already exists
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalStateException("User with email " + command.email() + " already exists");
        }

        // Create and save the new user
        User newUser = new User(command.email(), command.role());
        return userRepository.save(newUser);
    }

    /**
     * Get a user by ID
     * @param userId The user ID
     * @return An Optional with the user if found, otherwise an empty Optional
     */
    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Get a user by email
     * @param email The user's email address
     * @return An Optional with the user if found, otherwise an empty Optional
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

