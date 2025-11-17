package com.alpacaflow.meditrackplatform.devices.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Alert;
import com.alpacaflow.meditrackplatform.devices.domain.model.commands.CreateAlertCommand;
import com.alpacaflow.meditrackplatform.devices.domain.services.AlertCommandService;
import com.alpacaflow.meditrackplatform.devices.infrastructure.persistence.jpa.repositories.AlertRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AlertCommandService interface.
 * <p>This class is responsible for handling the commands related to the Alert aggregate. It requires an AlertRepository.</p>
 * @see AlertCommandService
 * @see AlertRepository
 */
@Service
public class AlertCommandServiceImpl implements AlertCommandService {
    private final AlertRepository alertRepository;

    public AlertCommandServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public Long handle(CreateAlertCommand command) {
        var alert = new Alert(command);
        try {
            alertRepository.save(alert);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving alert: %s".formatted(e.getMessage()));
        }
        return alert.getId();
    }
}

