package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Organization;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.*;

import java.util.Optional;

/**
 * OrganizationCommandService
 * Service that handles organization commands
 */
public interface OrganizationCommandService {
    /**
     * Handle a create organization command
     * @param command The create organization command containing the organization data
     * @return The created organization id
     * @see CreateOrganizationCommand
     */
    Long handle(CreateOrganizationCommand command);

    /**
     * Handle an update organization command
     * @param command The update organization command containing the organization data
     * @return The updated organization
     * @see UpdateOrganizationCommand
     */
    Optional<Organization> handle(UpdateOrganizationCommand command);

    /**
     * Handle a delete organization command
     * @param command The delete organization command containing the organization id
     * @see DeleteOrganizationCommand
     */
    void handle(DeleteOrganizationCommand command);
}

