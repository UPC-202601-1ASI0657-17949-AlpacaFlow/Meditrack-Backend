package com.alpacaflow.meditrackplatform.organization.application.internal.queryservices;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.SeniorCitizen;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllSeniorCitizensByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllSeniorCitizensQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetSeniorCitizenByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.SeniorCitizenQueryService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.SeniorCitizenRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the SeniorCitizenQueryService interface.
 */
@Service
public class SeniorCitizenQueryServiceImpl implements SeniorCitizenQueryService {
    private final SeniorCitizenRepository seniorCitizenRepository;

    /**
     * Constructor.
     *
     * @param seniorCitizenRepository the senior citizen repository
     * @see SeniorCitizenRepository
     */
    public SeniorCitizenQueryServiceImpl(SeniorCitizenRepository seniorCitizenRepository) {
        this.seniorCitizenRepository = seniorCitizenRepository;
    }

    // inherited javadoc
    @Override
    public Optional<SeniorCitizen> handle(GetSeniorCitizenByIdQuery query) {
        return seniorCitizenRepository.findById(query.seniorCitizenId());
    }

    // inherited javadoc
    @Override
    public List<SeniorCitizen> handle(GetAllSeniorCitizensQuery query) {
        return seniorCitizenRepository.findAll();
    }

    // inherited javadoc
    @Override
    public List<SeniorCitizen> handle(GetAllSeniorCitizensByOrganizationIdQuery query) {
        return seniorCitizenRepository.findByOrganization_Id(query.organizationId());
    }
}

