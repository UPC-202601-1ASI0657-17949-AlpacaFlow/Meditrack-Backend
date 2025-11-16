package com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Admin repository
 * <p>This interface is used to interact with the database and perform CRUD and business command and query supporting operations on the Admin entity.</p>
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    /**
     * This method is used to find all admins by organization id.
     * @param organizationId The organization id.
     * @return A list of admins belonging to the organization.
     */
    List<Admin> findByOrganization_Id(Long organizationId);

    /**
     * This method is used to find an admin by id and organization id.
     * @param id The admin id.
     * @param organizationId The organization id.
     * @return An optional with an admin object if found, otherwise an empty optional.
     */
    Optional<Admin> findByIdAndOrganization_Id(Long id, Long organizationId);

    /**
     * This method is used to find an admin by user id and organization id.
     * This ensures that the admin belongs to the specified organization.
     * @param userId The user id.
     * @param organizationId The organization id.
     * @return An optional with an admin object if found, otherwise an empty optional.
     */
    Optional<Admin> findByUserIdAndOrganization_Id(Long userId, Long organizationId);
}

