package com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Doctor repository
 * <p>This interface is used to interact with the database and perform CRUD and business command and query supporting operations on the Doctor entity.</p>
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    /**
     * This method is used to find all doctors by organization id.
     * @param organizationId The organization id.
     * @return A list of doctors belonging to the organization.
     */
    List<Doctor> findByOrganization_Id(Long organizationId);

    /**
     * This method is used to find a doctor by id and organization id.
     * @param id The doctor id.
     * @param organizationId The organization id.
     * @return An optional with a doctor object if found, otherwise an empty optional.
     */
    Optional<Doctor> findByIdAndOrganization_Id(Long id, Long organizationId);

    /**
     * This method is used to find a doctor by user id.
     * @param userId The user id.
     * @return An optional with a doctor object if found, otherwise an empty optional.
     */
    Optional<Doctor> findByUserId(Long userId);

    /**
     * This method is used to find a doctor by user id and organization id.
     * This ensures that the doctor belongs to the specified organization.
     * @param userId The user id.
     * @param organizationId The organization id.
     * @return An optional with a doctor object if found, otherwise an empty optional.
     */
    Optional<Doctor> findByUserIdAndOrganization_Id(Long userId, Long organizationId);

    boolean existsByOrganization_IdAndEmailIgnoreCase(Long organizationId, String email);

    boolean existsByOrganization_IdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(
            Long organizationId, String firstName, String lastName);

    boolean existsByOrganization_IdAndEmailIgnoreCaseAndIdNot(Long organizationId, String email, Long id);

    boolean existsByOrganization_IdAndFirstNameIgnoreCaseAndLastNameIgnoreCaseAndIdNot(
            Long organizationId, String firstName, String lastName, Long id);
}

