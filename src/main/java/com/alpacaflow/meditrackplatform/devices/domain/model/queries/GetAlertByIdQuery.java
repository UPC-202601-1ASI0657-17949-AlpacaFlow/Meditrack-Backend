package com.alpacaflow.meditrackplatform.devices.domain.model.queries;

/**
 * Query to get an alert by ID
 * @param alertId The alert ID
 */
public record GetAlertByIdQuery(Long alertId) {
    public GetAlertByIdQuery {
        if (alertId == null) {
            throw new IllegalArgumentException("Alert ID cannot be null");
        }
    }
}

