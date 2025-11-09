package com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.organization.domain.model.entities.DoctorAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DoctorAssignment repository
 * <p>This interface is used to interact with the database and perform CRUD operations on the DoctorAssignment entity.</p>
 */
@Repository
public interface DoctorAssignmentRepository extends JpaRepository<DoctorAssignment, Long> {
    /**
     * This method is used to find a doctor assignment by senior citizen id.
     * @param seniorCitizenId The senior citizen id.
     * @return An optional with a doctor assignment if found, otherwise an empty optional.
     */
    Optional<DoctorAssignment> findBySeniorCitizenId(Long seniorCitizenId);

    /**
     * This method is used to find all doctor assignments by doctor id.
     * @param doctorId The doctor id.
     * @return A list of doctor assignments for the doctor.
     */
    List<DoctorAssignment> findByDoctor_Id(Long doctorId);

    /**
     * This method is used to find a doctor assignment by doctor id and senior citizen id.
     * @param doctorId The doctor id.
     * @param seniorCitizenId The senior citizen id.
     * @return An optional with a doctor assignment if found, otherwise an empty optional.
     */
    Optional<DoctorAssignment> findByDoctor_IdAndSeniorCitizenId(Long doctorId, Long seniorCitizenId);

    /**
     * This method is used to delete a doctor assignment by senior citizen id.
     * @param seniorCitizenId The senior citizen id.
     */
    void deleteBySeniorCitizenId(Long seniorCitizenId);
}

