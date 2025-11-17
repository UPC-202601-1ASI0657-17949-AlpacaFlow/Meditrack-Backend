package com.alpacaflow.meditrackplatform.devices.domain.exceptions;

/**
 * Exception thrown when an alert is not found
 */
public class AlertNotFoundException extends RuntimeException {
    public AlertNotFoundException(Long alertId) {
        super("Alert with id %s not found".formatted(alertId));
    }
}

