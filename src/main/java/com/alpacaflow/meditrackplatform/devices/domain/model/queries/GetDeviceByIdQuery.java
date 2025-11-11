package com.alpacaflow.meditrackplatform.devices.domain.model.queries;

/**
 * Query to get a device by ID
 * @param deviceId The device ID
 */
public record GetDeviceByIdQuery(Long deviceId) {
    public GetDeviceByIdQuery {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
    }
}

