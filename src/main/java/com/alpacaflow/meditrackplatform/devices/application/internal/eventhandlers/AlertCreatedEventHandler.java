package com.alpacaflow.meditrackplatform.devices.application.internal.eventhandlers;

import com.alpacaflow.meditrackplatform.devices.domain.model.commands.CreateAlertCommand;
import com.alpacaflow.meditrackplatform.devices.domain.model.events.AlertCreatedEvent;
import com.alpacaflow.meditrackplatform.devices.domain.services.AlertCommandService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Event handler for AlertCreatedEvent
 * <p>This class listens for AlertCreatedEvent and creates an alert in the system.</p>
 */
@Service
public class AlertCreatedEventHandler {
    private final AlertCommandService alertCommandService;

    public AlertCreatedEventHandler(AlertCommandService alertCommandService) {
        this.alertCommandService = alertCommandService;
    }

    /**
     * Handle the AlertCreatedEvent by creating an alert
     * @param event The AlertCreatedEvent
     */
    @EventListener
    public void on(AlertCreatedEvent event) {
        var command = new CreateAlertCommand(
                event.deviceId(),
                event.dataRegistered(),
                event.measurementType()
        );
        alertCommandService.handle(command);
    }
}

