package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Admin;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllAdminsByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllAdminsQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAdminByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAdminByUserIdAndOrganizationIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * AdminQueryService
 * Service that handles admin queries
 */
public interface AdminQueryService {
    /**
     * Handle a get admin by id query
     * @param query The get admin by id query containing the admin id
     * @return The admin
     * @see GetAdminByIdQuery
     */
    Optional<Admin> handle(GetAdminByIdQuery query);

    /**
     * Handle a get all admins query
     * @param query The get all admins query
     * @return The list of admins
     * @see GetAllAdminsQuery
     */
    List<Admin> handle(GetAllAdminsQuery query);

    /**
     * Handle a get all admins by organization id query
     * @param query The get all admins by organization id query containing the organization id
     * @return The list of admins belonging to the organization
     * @see GetAllAdminsByOrganizationIdQuery
     */
    List<Admin> handle(GetAllAdminsByOrganizationIdQuery query);

    /**
     * Handle a get admin by user id and organization id query
     * @param query The get admin by user id and organization id query
     * @return The admin if found in the specified organization
     * @see GetAdminByUserIdAndOrganizationIdQuery
     */
    Optional<Admin> handle(GetAdminByUserIdAndOrganizationIdQuery query);
}