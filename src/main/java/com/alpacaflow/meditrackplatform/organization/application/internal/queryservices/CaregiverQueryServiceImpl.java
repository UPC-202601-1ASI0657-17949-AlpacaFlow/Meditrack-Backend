package com.alpacaflow.meditrackplatform.organization.application.internal.queryservices;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Caregiver;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllCaregiversByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllCaregiversQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetCaregiverByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetCaregiverByUserIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetCaregiverByUserIdAndOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.CaregiverQueryService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.CaregiverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the CaregiverQueryService interface.
 */
@Service
public class CaregiverQueryServiceImpl implements CaregiverQueryService {
    private final CaregiverRepository caregiverRepository;

    /**
     * Constructor.
     *
     * @param caregiverRepository the caregiver repository
     * @see CaregiverRepository
     */
    public CaregiverQueryServiceImpl(CaregiverRepository caregiverRepository) {
        this.caregiverRepository = caregiverRepository;
    }

    // inherited javadoc
    @Override
    public Optional<Caregiver> handle(GetCaregiverByIdQuery query) {
        return caregiverRepository.findById(query.caregiverId());
    }

    // inherited javadoc
    @Override
    public List<Caregiver> handle(GetAllCaregiversQuery query) {
        return caregiverRepository.findAll();
    }

    // inherited javadoc
    @Override
    public List<Caregiver> handle(GetAllCaregiversByOrganizationIdQuery query) {
        return caregiverRepository.findByOrganization_Id(query.organizationId());
    }

    // inherited javadoc
    @Override
    public Optional<Caregiver> handle(GetCaregiverByUserIdQuery query) {
        return caregiverRepository.findByUserId(query.userId());
    }

    // inherited javadoc
    @Override
    public Optional<Caregiver> handle(GetCaregiverByUserIdAndOrganizationIdQuery query) {
        return caregiverRepository.findByUserIdAndOrganization_Id(query.userId(), query.organizationId());
    }
}

