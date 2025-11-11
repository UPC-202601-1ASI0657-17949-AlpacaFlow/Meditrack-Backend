package com.alpacaflow.meditrackplatform.devices.domain.services;

import com.alpacaflow.meditrackplatform.devices.domain.model.commands.CreateAlertCommand;

/**
 * AlertCommandService
 * Service that handles alert commands
 */
public interface AlertCommandService {
    /**
     * Handle a create alert command
     * @param command The create alert command containing the alert data
     * @return The ID of the created alert
     * @see CreateAlertCommand
     */
    Long handle(CreateAlertCommand command);
}

