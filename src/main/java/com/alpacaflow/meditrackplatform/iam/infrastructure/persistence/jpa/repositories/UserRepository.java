package com.alpacaflow.meditrackplatform.iam.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.iam.domain.model.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
     * Match by canonical email: {@code lower(trim(column))} equals the given value.
     *
     * @param normalizedEmail value must already be {@code trim().toLowerCase(Locale.ROOT)}
     */
    @Query("select u from User u where lower(trim(u.email)) = :normalizedEmail")
    Optional<User> findByNormalizedEmail(@Param("normalizedEmail") String normalizedEmail);
}
