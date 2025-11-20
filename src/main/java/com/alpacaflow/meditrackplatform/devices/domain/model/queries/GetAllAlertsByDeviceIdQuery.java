package com.alpacaflow.meditrackplatform.devices.domain.model.queries;

/**
 * Query to get all alerts by device ID
 * @param deviceId The device ID
 */
public record GetAllAlertsByDeviceIdQuery(Long deviceId) {
    public GetAllAlertsByDeviceIdQuery {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
    }
}
