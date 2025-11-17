package com.alpacaflow.meditrackplatform.devices.domain.model.queries;

/**
 * Query to get all blood pressure measurements by device ID
 * @param deviceId The device ID
 */
public record GetAllBloodPressureMeasurementsByDeviceIdQuery(Long deviceId) {
    public GetAllBloodPressureMeasurementsByDeviceIdQuery {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
    }
}

