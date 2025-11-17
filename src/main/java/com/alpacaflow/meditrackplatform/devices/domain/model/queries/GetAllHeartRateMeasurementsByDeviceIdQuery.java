package com.alpacaflow.meditrackplatform.devices.domain.model.queries;

/**
 * Query to get all heart rate measurements by device ID
 * @param deviceId The device ID
 */
public record GetAllHeartRateMeasurementsByDeviceIdQuery(Long deviceId) {
    public GetAllHeartRateMeasurementsByDeviceIdQuery {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
    }
}

