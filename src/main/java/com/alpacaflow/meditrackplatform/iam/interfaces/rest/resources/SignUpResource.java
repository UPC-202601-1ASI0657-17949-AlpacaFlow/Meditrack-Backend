package com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources;

public record SignUpResource(String email, String password, String role) {
    /**
     * Constructor for sign up without role
     */
    public SignUpResource(String email, String password) {
        this(email, password, null);
    }
}

