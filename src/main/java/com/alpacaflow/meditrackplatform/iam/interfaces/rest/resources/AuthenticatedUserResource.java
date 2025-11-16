package com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(Long id, String email, String role, String token) {
}

