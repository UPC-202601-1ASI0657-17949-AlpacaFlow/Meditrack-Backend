package com.alpacaflow.meditrackplatform.iam.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.iam.application.internal.outboundservices.hashing.HashingService;
import com.alpacaflow.meditrackplatform.iam.application.internal.outboundservices.tokens.TokenService;
import com.alpacaflow.meditrackplatform.iam.domain.model.aggregates.User;
import com.alpacaflow.meditrackplatform.iam.domain.model.commands.CreateMockUserCommand;
import com.alpacaflow.meditrackplatform.iam.domain.model.commands.SignInCommand;
import com.alpacaflow.meditrackplatform.iam.domain.model.commands.SignUpCommand;
import com.alpacaflow.meditrackplatform.iam.domain.services.UserCommandService;
import com.alpacaflow.meditrackplatform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.CreateAdminCommand;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.CreateOrganizationCommand;
import com.alpacaflow.meditrackplatform.organization.domain.services.AdminCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.services.OrganizationCommandService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the UserCommandService interface.
 * <p>This class is responsible for handling the commands related to the User aggregate.</p>
 * 
 * @see UserCommandService
 * @see UserRepository
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final OrganizationCommandService organizationCommandService;
    private final AdminCommandService adminCommandService;

    /**
     * Constructor of the class.
     * @param userRepository the repository to be used by the class
     * @param hashingService the hashing service for password encoding/validation
     * @param tokenService the token service for JWT generation
     * @param organizationCommandService the organization command service (lazy to avoid circular dependency)
     * @param adminCommandService the admin command service (lazy to avoid circular dependency)
     */
    public UserCommandServiceImpl(
            UserRepository userRepository, 
            HashingService hashingService, 
            TokenService tokenService,
            @Lazy OrganizationCommandService organizationCommandService,
            @Lazy AdminCommandService adminCommandService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.organizationCommandService = organizationCommandService;
        this.adminCommandService = adminCommandService;
    }

    /**
     * Handle a sign-in command
     * @param command The sign-in command containing email and password
     * @return An Optional with a pair of User and JWT token if authentication succeeds
     * @throws RuntimeException if user not found or password is invalid
     */
    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByEmail(command.email());
        if (user.isEmpty())
            throw new RuntimeException("User not found");
        if (!hashingService.matches(command.password(), user.get().getPassword()))
            throw new RuntimeException("Invalid password");
        var token = tokenService.generateToken(user.get().getEmail()); // Use email as username
        return Optional.of(ImmutablePair.of(user.get(), token));
    }

    /**
     * Handle a sign-up command
     * @param command The sign-up command containing email, password and optional role
     * @return An Optional with the created user
     * @throws RuntimeException if user with email already exists
     */
    @Override
    @Transactional
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByEmail(command.email()))
            throw new RuntimeException("Email already exists");
        
        var hashedPassword = hashingService.encode(command.password());
        var user = new User(command.email(), hashedPassword, command.role());
        var savedUser = userRepository.save(user);
        
        // If role is admin, create organization and admin entities
        if ("admin".equalsIgnoreCase(command.role())) {
            // Validate required fields for admin sign-up
            if (command.firstName() == null || command.firstName().isBlank()) {
                throw new RuntimeException("First name is required for admin sign-up");
            }
            if (command.lastName() == null || command.lastName().isBlank()) {
                throw new RuntimeException("Last name is required for admin sign-up");
            }
            if (command.organizationName() == null || command.organizationName().isBlank()) {
                throw new RuntimeException("Organization name is required for admin sign-up");
            }
            if (command.organizationType() == null || command.organizationType().isBlank()) {
                throw new RuntimeException("Organization type is required for admin sign-up");
            }
            
            // Create organization
            var createOrgCommand = new CreateOrganizationCommand(
                    command.organizationName(),
                    command.organizationType()
            );
            var organizationId = organizationCommandService.handle(createOrgCommand);
            
            // Create admin linked to the organization and user
            var createAdminCommand = new CreateAdminCommand(
                    organizationId,
                    savedUser.getId(),
                    command.firstName(),
                    command.lastName()
            );
            adminCommandService.handle(createAdminCommand);
        }
        
        return Optional.of(savedUser);
    }

    /**
     * Handle a create mock user command (for development/testing)
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

        // Create and save the new user with hashed password
        // Note: This is for development only. In production, all users should have passwords.
        // For development: use email as password for easier login (e.g., doctor@clinic.com / doctor@clinic.com)
        // In production, this should be changed to a secure password generation mechanism
        var hashedPassword = hashingService.encode(command.email());
        User newUser = new User(command.email(), hashedPassword, command.role());
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

