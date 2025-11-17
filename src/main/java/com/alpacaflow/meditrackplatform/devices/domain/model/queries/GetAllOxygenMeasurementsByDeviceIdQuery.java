package com.alpacaflow.meditrackplatform.devices.domain.model.queries;

/**
 * Query to get all oxygen saturation measurements by device ID
 * @param deviceId The device ID
 */
public record GetAllOxygenMeasurementsByDeviceIdQuery(Long deviceId) {
    public GetAllOxygenMeasurementsByDeviceIdQuery {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
    }
}

