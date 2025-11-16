package com.alpacaflow.meditrackplatform.organization.application.internal.queryservices;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Doctor;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllDoctorsByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllDoctorsQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetDoctorByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetDoctorByUserIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetDoctorByUserIdAndOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.DoctorQueryService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the DoctorQueryService interface.
 */
@Service
public class DoctorQueryServiceImpl implements DoctorQueryService {
    private final DoctorRepository doctorRepository;

    /**
     * Constructor.
     *
     * @param doctorRepository the doctor repository
     * @see DoctorRepository
     */
    public DoctorQueryServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // inherited javadoc
    @Override
    public Optional<Doctor> handle(GetDoctorByIdQuery query) {
        return doctorRepository.findById(query.doctorId());
    }

    // inherited javadoc
    @Override
    public List<Doctor> handle(GetAllDoctorsQuery query) {
        return doctorRepository.findAll();
    }

    // inherited javadoc
    @Override
    public List<Doctor> handle(GetAllDoctorsByOrganizationIdQuery query) {
        return doctorRepository.findByOrganization_Id(query.organizationId());
    }

    // inherited javadoc
    @Override
    public Optional<Doctor> handle(GetDoctorByUserIdQuery query) {
        return doctorRepository.findByUserId(query.userId());
    }

    // inherited javadoc
    @Override
    public Optional<Doctor> handle(GetDoctorByUserIdAndOrganizationIdQuery query) {
        return doctorRepository.findByUserIdAndOrganization_Id(query.userId(), query.organizationId());
    }
}

