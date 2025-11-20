package com.alpacaflow.meditrackplatform.devices.domain.services;

import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Alert;
import com.alpacaflow.meditrackplatform.devices.domain.model.queries.GetAlertByIdQuery;
import com.alpacaflow.meditrackplatform.devices.domain.model.queries.GetAllAlertsQuery;

import java.util.List;
import java.util.Optional;

/**
 * AlertQueryService
 * Service that handles alert queries
 */
public interface AlertQueryService {
    /**
     * Handle a get alert by id query
     * @param query The query containing the alert id
     * @return The alert if found
     * @see GetAlertByIdQuery
     */
    Optional<Alert> handle(GetAlertByIdQuery query);

    /**
     * Handle a get all alerts query
     * @param query The query
     * @return List of all alerts
     * @see GetAllAlertsQuery
     */
    List<Alert> handle(GetAllAlertsQuery query);

    /**
     * Handle a get all alerts by device ID query
     * @param query The query containing the device ID
     * @return List of alerts for the device
     * @see com.alpacaflow.meditrackplatform.devices.domain.model.queries.GetAllAlertsByDeviceIdQuery
     */
    List<Alert> handle(com.alpacaflow.meditrackplatform.devices.domain.model.queries.GetAllAlertsByDeviceIdQuery query);
}

