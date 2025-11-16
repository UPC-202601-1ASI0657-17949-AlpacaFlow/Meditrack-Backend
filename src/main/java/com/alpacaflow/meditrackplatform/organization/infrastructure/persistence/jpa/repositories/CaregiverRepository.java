package com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Caregiver repository
 * <p>This interface is used to interact with the database and perform CRUD and business command and query supporting operations on the Caregiver entity.</p>
 */
@Repository
public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
    /**
     * This method is used to find all caregivers by organization id.
     * @param organizationId The organization id.
     * @return A list of caregivers belonging to the organization.
     */
    List<Caregiver> findByOrganization_Id(Long organizationId);

    /**
     * This method is used to find a caregiver by id and organization id.
     * @param id The caregiver id.
     * @param organizationId The organization id.
     * @return An optional with a caregiver object if found, otherwise an empty optional.
     */
    Optional<Caregiver> findByIdAndOrganization_Id(Long id, Long organizationId);

    /**
     * This method is used to find a caregiver by user id.
     * @param userId The user id.
     * @return An optional with a caregiver object if found, otherwise an empty optional.
     */
    Optional<Caregiver> findByUserId(Long userId);

    /**
     * This method is used to find a caregiver by user id and organization id.
     * This ensures that the caregiver belongs to the specified organization.
     * @param userId The user id.
     * @param organizationId The organization id.
     * @return An optional with a caregiver object if found, otherwise an empty optional.
     */
    Optional<Caregiver> findByUserIdAndOrganization_Id(Long userId, Long organizationId);
}

