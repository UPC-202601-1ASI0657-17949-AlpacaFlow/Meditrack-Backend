package com.alpacaflow.meditrackplatform.iam.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.iam.domain.model.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository
 * <p>This interface is used to interact with the database and perform CRUD operations on the User entity.</p>
 * 
 * <p><strong>Note:</strong> This is part of a temporary mock implementation for development purposes.
 * In production, this should be replaced by the actual IAM bounded context repository.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by email address
     * @param email the email address to search for
     * @return an Optional with a User if found, otherwise an empty Optional
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email address
     * @param email the email address to check
     * @return true if a user exists with the given email, false otherwise
     */
    boolean existsByEmail(String email);
}

