package com.alpacaflow.meditrackplatform.iam.domain.model.aggregates;

import com.alpacaflow.meditrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * User aggregate representing a user in the IAM (Identity and Access Management) context.
 * This entity supports authentication with email (used as username) and password.
 */
@Entity
@Getter
@Setter
public class User extends AuditableAbstractAggregateRoot<User> {

    @NotBlank
    @Size(max = 100)
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 120)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = true)
    private String role;

    /**
     * Default constructor for JPA
     */
    public User() {
        super();
    }

    /**
     * Constructor for creating a new User with email and password
     * @param email the user's email address (must be unique, used as username)
     * @param password the user's password (will be hashed)
     */
    public User(String email, String password) {
        this();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor for creating a new User with email, password and role
     * @param email the user's email address (must be unique, used as username)
     * @param password the user's password (will be hashed)
     * @param role the user's role (optional, e.g., "admin", "doctor", "caregiver")
     */
    public User(String email, String password, String role) {
        this(email, password);
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
     * Updates the user's password
     * @param password the new password (will be hashed)
     * @return this user instance
     */
    public User updatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        this.password = password;
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

