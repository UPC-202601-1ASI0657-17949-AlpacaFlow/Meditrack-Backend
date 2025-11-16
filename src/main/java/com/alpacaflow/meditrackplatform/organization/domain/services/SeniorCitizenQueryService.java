package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.SeniorCitizen;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllSeniorCitizensByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllSeniorCitizensQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetSeniorCitizenByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * SeniorCitizenQueryService
 * Service that handles senior citizen queries
 */
public interface SeniorCitizenQueryService {
    /**
     * Handle a get senior citizen by id query
     * @param query The get senior citizen by id query containing the senior citizen id
     * @return The senior citizen
     * @see GetSeniorCitizenByIdQuery
     */
    Optional<SeniorCitizen> handle(GetSeniorCitizenByIdQuery query);

    /**
     * Handle a get all senior citizens query
     * @param query The get all senior citizens query
     * @return The list of senior citizens
     * @see GetAllSeniorCitizensQuery
     */
    List<SeniorCitizen> handle(GetAllSeniorCitizensQuery query);

    /**
     * Handle a get all senior citizens by organization id query
     * @param query The get all senior citizens by organization id query containing the organization id
     * @return The list of senior citizens belonging to the organization
     * @see GetAllSeniorCitizensByOrganizationIdQuery
     */
    List<SeniorCitizen> handle(GetAllSeniorCitizensByOrganizationIdQuery query);
}

