package com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.SeniorCitizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SeniorCitizen repository
 * <p>This interface is used to interact with the database and perform CRUD and business command and query supporting operations on the SeniorCitizen entity.</p>
 */
@Repository
public interface SeniorCitizenRepository extends JpaRepository<SeniorCitizen, Long> {
    /**
     * This method is used to find all senior citizens by organization id.
     * @param organizationId The organization id.
     * @return A list of senior citizens belonging to the organization.
     */
    List<SeniorCitizen> findByOrganization_Id(Long organizationId);

    /**
     * This method is used to find a senior citizen by id and organization id.
     * @param id The senior citizen id.
     * @param organizationId The organization id.
     * @return An optional with a senior citizen object if found, otherwise an empty optional.
     */
    Optional<SeniorCitizen> findByIdAndOrganization_Id(Long id, Long organizationId);

    /**
     * This method is used to find all senior citizens assigned to a specific doctor.
     * @param doctorId The doctor id.
     * @return A list of senior citizens assigned to the doctor.
     */
    List<SeniorCitizen> findByAssignedDoctorId(Long doctorId);

    boolean existsByOrganization_IdAndDni(Long organizationId, String dni);

    boolean existsByOrganization_IdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(
            Long organizationId, String firstName, String lastName);

    boolean existsByOrganization_IdAndDniAndIdNot(Long organizationId, String dni, Long id);

    boolean existsByOrganization_IdAndFirstNameIgnoreCaseAndLastNameIgnoreCaseAndIdNot(
            Long organizationId, String firstName, String lastName, Long id);

    boolean existsByDeviceId(Long deviceId);

    boolean existsByDeviceIdAndIdNot(Long deviceId, Long id);
}

