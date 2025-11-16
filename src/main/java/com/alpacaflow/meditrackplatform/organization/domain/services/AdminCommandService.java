package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Admin;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.CreateAdminCommand;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.DeleteAdminCommand;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.UpdateAdminCommand;

import java.util.Optional;

/**
 * AdminCommandService
 * Service that handles admin commands
 */
public interface AdminCommandService {
    /**
     * Handle a create admin command
     * @param command The create admin command containing the admin data
     * @return The created admin id
     * @see CreateAdminCommand
     */
    Long handle(CreateAdminCommand command);

    /**
     * Handle an update admin command
     * @param command The update admin command containing the admin data
     * @return The updated admin
     * @see UpdateAdminCommand
     */
    Optional<Admin> handle(UpdateAdminCommand command);

    /**
     * Handle a delete admin command
     * @param command The delete admin command containing the admin id
     * @see DeleteAdminCommand
     */
    void handle(DeleteAdminCommand command);
}

