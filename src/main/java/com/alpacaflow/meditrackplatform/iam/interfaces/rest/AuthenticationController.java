package com.alpacaflow.meditrackplatform.iam.interfaces.rest;

import com.alpacaflow.meditrackplatform.iam.domain.services.UserCommandService;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources.SignInResource;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources.SignUpResource;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources.UserResource;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Available Authentication Endpoints")
public class AuthenticationController {
    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign-in", description = "Sign-in with the provided credentials (email and password).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")})
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource signInResource) {
        var signInCommand = SignInCommandFromResourceAssembler.toCommandFromResource(signInResource);
        var authenticatedUser = userCommandService.handle(signInCommand);
        if (authenticatedUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(
                authenticatedUser.get().getLeft(), 
                authenticatedUser.get().getRight());
        return ResponseEntity.ok(authenticatedUserResource);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "Sign-up", description = "Sign-up with the provided credentials (email, password and optional role).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.")})
    public ResponseEntity<AuthenticatedUserResource> signUp(@RequestBody SignUpResource signUpResource) {
        var signUpCommand = SignUpCommandFromResourceAssembler.toCommandFromResource(signUpResource);
        var user = userCommandService.handle(signUpCommand);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        // After sign-up, generate token and return authenticated user resource
        var signInCommand = new com.alpacaflow.meditrackplatform.iam.domain.model.commands.SignInCommand(
                signUpResource.email(), 
                signUpResource.password()
        );
        var authenticatedUser = userCommandService.handle(signInCommand);
        if (authenticatedUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(
                authenticatedUser.get().getLeft(), 
                authenticatedUser.get().getRight());
        return new ResponseEntity<>(authenticatedUserResource, HttpStatus.CREATED);
    }
}

