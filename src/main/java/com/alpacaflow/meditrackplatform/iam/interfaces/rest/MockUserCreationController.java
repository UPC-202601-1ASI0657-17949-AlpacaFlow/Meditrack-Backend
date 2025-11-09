package com.alpacaflow.meditrackplatform.iam.interfaces.rest;

import com.alpacaflow.meditrackplatform.iam.domain.services.UserCommandService;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources.CreateMockUserResource;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources.UserResource;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.transform.CreateMockUserCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Mock user creation controller for development purposes.
 * 
 * <p><strong>IMPORTANT:</strong> This controller is only available in the "dev" profile.
 * It will NOT be available in production environments.</p>
 * 
 * <p>This controller allows creating mock users to support testing of the Organization
 * bounded context, which requires valid user_id references.</p>
 * 
 * <p><strong>Usage:</strong></p>
 * <ol>
 *   <li>Create a mock user: POST /temp-api/v1/users/create</li>
 *   <li>Use the returned userId when creating Admins, Doctors, or Caregivers</li>
 * </ol>
 * 
 * <p><strong>Note:</strong> This is a temporary solution. In production, user creation
 * should be handled by the actual IAM bounded context.</p>
 */
@RestController
@RequestMapping(value = "/temp-api/v1/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Mock Users (Dev Only)", description = "Temporary endpoints for creating mock users during development. Only available in dev profile.")
@Profile("dev") // <-- CRITICAL: Only available in development profile
public class MockUserCreationController {
    private final UserCommandService userCommandService;

    /**
     * Instantiates a new {@link MockUserCreationController} instance.
     *
     * @param userCommandService The {@link UserCommandService} instance
     */
    public MockUserCreationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    /**
     * Create a new mock user
     *
     * @param resource The {@link CreateMockUserResource} instance
     * @return The {@link UserResource} resource for the created user, or a bad request response if the user was not created
     */
    @PostMapping("/create")
    @Operation(
            summary = "Create a new mock user (Dev Only)",
            description = "Create a new mock user for development/testing purposes. This endpoint is only available in the 'dev' profile."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid input or user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResource> createMockUser(@RequestBody CreateMockUserResource resource) {
        try {
            var createMockUserCommand = CreateMockUserCommandFromResourceAssembler.toCommandFromResource(resource);
            var createdUser = userCommandService.handle(createMockUserCommand);
            var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(createdUser);
            return new ResponseEntity<>(userResource, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            // User already exists
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            // Invalid input
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Other errors
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get a user by ID
     *
     * @param userId The user ID
     * @return The {@link UserResource} resource for the user, or a not found response if the user was not found
     */
    @GetMapping("/{userId}")
    @Operation(
            summary = "Get user by ID (Dev Only)",
            description = "Get a user by ID. This endpoint is only available in the 'dev' profile."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var user = userCommandService.getUserById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Get a user by email
     *
     * @param email The user's email address
     * @return The {@link UserResource} resource for the user, or a not found response if the user was not found
     */
    @GetMapping("/email/{email}")
    @Operation(
            summary = "Get user by email (Dev Only)",
            description = "Get a user by email address. This endpoint is only available in the 'dev' profile."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResource> getUserByEmail(@PathVariable String email) {
        var user = userCommandService.getUserByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }
}

