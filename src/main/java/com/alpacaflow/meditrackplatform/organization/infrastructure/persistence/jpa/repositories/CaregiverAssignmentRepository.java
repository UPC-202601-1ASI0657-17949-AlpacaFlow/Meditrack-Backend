package com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.organization.domain.model.entities.CaregiverAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CaregiverAssignment repository
 * <p>This interface is used to interact with the database and perform CRUD operations on the CaregiverAssignment entity.</p>
 */
@Repository
public interface CaregiverAssignmentRepository extends JpaRepository<CaregiverAssignment, Long> {
    /**
     * This method is used to find a caregiver assignment by senior citizen id.
     * @param seniorCitizenId The senior citizen id.
     * @return An optional with a caregiver assignment object if found, otherwise an empty optional.
     */
    Optional<CaregiverAssignment> findBySeniorCitizenId(Long seniorCitizenId);

    /**
     * This method is used to delete a caregiver assignment by senior citizen id.
     * @param seniorCitizenId The senior citizen id.
     */
    void deleteBySeniorCitizenId(Long seniorCitizenId);
}

