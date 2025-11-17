package com.alpacaflow.meditrackplatform.devices.domain.model.queries;

/**
 * Query to get all temperature measurements by device ID
 * @param deviceId The device ID
 */
public record GetAllTemperatureMeasurementsByDeviceIdQuery(Long deviceId) {
    public GetAllTemperatureMeasurementsByDeviceIdQuery {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
    }
}

