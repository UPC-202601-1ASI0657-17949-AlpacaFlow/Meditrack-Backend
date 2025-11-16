package com.alpacaflow.meditrackplatform.organization.application.internal.queryservices;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Organization;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllOrganizationsQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetOrganizationByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.OrganizationQueryService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the OrganizationQueryService interface.
 */
@Service
public class OrganizationQueryServiceImpl implements OrganizationQueryService {
    private final OrganizationRepository organizationRepository;

    /**
     * Constructor.
     *
     * @param organizationRepository the organization repository
     * @see OrganizationRepository
     */
    public OrganizationQueryServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    // inherited javadoc
    @Override
    public Optional<Organization> handle(GetOrganizationByIdQuery query) {
        return organizationRepository.findById(query.organizationId());
    }

    // inherited javadoc
    @Override
    public List<Organization> handle(GetAllOrganizationsQuery query) {
        return organizationRepository.findAll();
    }
}

