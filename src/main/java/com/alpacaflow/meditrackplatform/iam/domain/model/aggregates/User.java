package com.alpacaflow.meditrackplatform.iam.domain.model.aggregates;

import com.alpacaflow.meditrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

/**
 * User aggregate representing a user in the IAM (Identity and Access Management) context.
 * This is a minimal implementation to support the Organization context during development.
 * 
 * <p>This entity simulates the IAM bounded context and allows the Organization context
 * to validate user_id references without requiring a full IAM implementation.</p>
 * 
 * <p><strong>Note:</strong> This is a temporary mock implementation for development purposes.
 * In production, this should be replaced by the actual IAM bounded context.</p>
 */
@Entity
@Getter
public class User extends AuditableAbstractAggregateRoot<User> {

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "role", nullable = true)
    private String role;

    /**
     * Default constructor for JPA
     */
    public User() {
        super();
    }

    /**
     * Constructor for creating a new User
     * @param email the user's email address (must be unique)
     * @param role the user's role (optional, e.g., "admin", "doctor", "caregiver")
     */
    public User(String email, String role) {
        this();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        this.email = email;
        this.role = role; // Can be null
    }

    /**
     * Updates the user's email address
     * @param email the new email address
     * @return this user instance
     */
    public User updateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        this.email = email;
        return this;
    }

    /**
     * Updates the user's role
     * @param role the new role
     * @return this user instance
     */
    public User updateRole(String role) {
        this.role = role;
        return this;
    }

}

