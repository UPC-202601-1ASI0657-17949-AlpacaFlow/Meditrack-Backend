package com.alpacaflow.meditrackplatform.devices.application.internal.queryservices;

import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Alert;
import com.alpacaflow.meditrackplatform.devices.domain.model.queries.GetAlertByIdQuery;
import com.alpacaflow.meditrackplatform.devices.domain.model.queries.GetAllAlertsQuery;
import com.alpacaflow.meditrackplatform.devices.domain.services.AlertQueryService;
import com.alpacaflow.meditrackplatform.devices.infrastructure.persistence.jpa.repositories.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the AlertQueryService interface.
 * <p>This class is responsible for handling the queries related to the Alert aggregate. It requires an AlertRepository.</p>
 * @see AlertQueryService
 * @see AlertRepository
 */
@Service
public class AlertQueryServiceImpl implements AlertQueryService {
    private final AlertRepository alertRepository;

    public AlertQueryServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public Optional<Alert> handle(GetAlertByIdQuery query) {
        return alertRepository.findById(query.alertId());
    }

    @Override
    public List<Alert> handle(GetAllAlertsQuery query) {
        return alertRepository.findAll();
    }
}

