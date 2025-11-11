package com.alpacaflow.meditrackplatform.devices.domain.exceptions;

/**
 * Exception thrown when a device is not found
 */
public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(Long deviceId) {
        super("Device with id %s not found".formatted(deviceId));
    }
}

