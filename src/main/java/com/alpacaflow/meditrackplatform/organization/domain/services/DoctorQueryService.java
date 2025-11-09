package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Doctor;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllDoctorsByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllDoctorsQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetDoctorByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetDoctorByUserIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetDoctorByUserIdAndOrganizationIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * DoctorQueryService
 * Service that handles doctor queries
 */
public interface DoctorQueryService {
    /**
     * Handle a get doctor by id query
     * @param query The get doctor by id query containing the doctor id
     * @return The doctor
     * @see GetDoctorByIdQuery
     */
    Optional<Doctor> handle(GetDoctorByIdQuery query);

    /**
     * Handle a get all doctors query
     * @param query The get all doctors query
     * @return The list of doctors
     * @see GetAllDoctorsQuery
     */
    List<Doctor> handle(GetAllDoctorsQuery query);

    /**
     * Handle a get all doctors by organization id query
     * @param query The get all doctors by organization id query containing the organization id
     * @return The list of doctors belonging to the organization
     * @see GetAllDoctorsByOrganizationIdQuery
     */
    List<Doctor> handle(GetAllDoctorsByOrganizationIdQuery query);

    /**
     * Handle a get doctor by user id query
     * @param query The get doctor by user id query containing the user id
     * @return The doctor
     * @see GetDoctorByUserIdQuery
     */
    Optional<Doctor> handle(GetDoctorByUserIdQuery query);

    /**
     * Handle a get doctor by user id and organization id query
     * @param query The get doctor by user id and organization id query
     * @return The doctor if found in the specified organization
     * @see GetDoctorByUserIdAndOrganizationIdQuery
     */
    Optional<Doctor> handle(GetDoctorByUserIdAndOrganizationIdQuery query);
}

