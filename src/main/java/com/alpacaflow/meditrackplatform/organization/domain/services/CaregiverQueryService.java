package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Caregiver;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllCaregiversByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllCaregiversQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetCaregiverByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetCaregiverByUserIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetCaregiverByUserIdAndOrganizationIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * CaregiverQueryService
 * Service that handles caregiver queries
 */
public interface CaregiverQueryService {
    /**
     * Handle a get caregiver by id query
     * @param query The get caregiver by id query containing the caregiver id
     * @return The caregiver
     * @see GetCaregiverByIdQuery
     */
    Optional<Caregiver> handle(GetCaregiverByIdQuery query);

    /**
     * Handle a get all caregivers query
     * @param query The get all caregivers query
     * @return The list of caregivers
     * @see GetAllCaregiversQuery
     */
    List<Caregiver> handle(GetAllCaregiversQuery query);

    /**
     * Handle a get all caregivers by organization id query
     * @param query The get all caregivers by organization id query containing the organization id
     * @return The list of caregivers belonging to the organization
     * @see GetAllCaregiversByOrganizationIdQuery
     */
    List<Caregiver> handle(GetAllCaregiversByOrganizationIdQuery query);

    /**
     * Handle a get caregiver by user id query
     * @param query The get caregiver by user id query containing the user id
     * @return The caregiver
     * @see GetCaregiverByUserIdQuery
     */
    Optional<Caregiver> handle(GetCaregiverByUserIdQuery query);

    /**
     * Handle a get caregiver by user id and organization id query
     * @param query The get caregiver by user id and organization id query
     * @return The caregiver if found in the specified organization
     * @see GetCaregiverByUserIdAndOrganizationIdQuery
     */
    Optional<Caregiver> handle(GetCaregiverByUserIdAndOrganizationIdQuery query);
}

