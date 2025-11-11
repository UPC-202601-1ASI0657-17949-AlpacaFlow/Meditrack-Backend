package com.alpacaflow.meditrackplatform.devices.domain.model.commands;

/**
 * Command to create an alert
 * @param deviceId The device ID that generated the alert
 * @param dataRegistered The measurement value that triggered the alert
 * @param measurementType The type of measurement
 */
public record CreateAlertCommand(Long deviceId, double dataRegistered, String measurementType) {
    public CreateAlertCommand {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
        if (measurementType == null || measurementType.isBlank()) {
            throw new IllegalArgumentException("Measurement type cannot be null or blank");
        }
    }
}

