package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Organization;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllOrganizationsQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetOrganizationByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * OrganizationQueryService
 * Service that handles organization queries
 */
public interface OrganizationQueryService {
    /**
     * Handle a get organization by id query
     * @param query The get organization by id query containing the organization id
     * @return The organization
     * @see GetOrganizationByIdQuery
     */
    Optional<Organization> handle(GetOrganizationByIdQuery query);

    /**
     * Handle a get all organizations query
     * @param query The get all organizations query
     * @return The list of organizations
     * @see GetAllOrganizationsQuery
     */
    List<Organization> handle(GetAllOrganizationsQuery query);
}

