package com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources;

public record SignUpResource(
        String email, 
        String password, 
        String role,
        String firstName,
        String lastName,
        String organizationName,
        String organizationType
) {
    /**
     * Constructor for sign up without role (backward compatibility)
     */
    public SignUpResource(String email, String password) {
        this(email, password, null, null, null, null, null);
    }
    
    /**
     * Constructor for sign up with role only (backward compatibility)
     */
    public SignUpResource(String email, String password, String role) {
        this(email, password, role, null, null, null, null);
    }
}

