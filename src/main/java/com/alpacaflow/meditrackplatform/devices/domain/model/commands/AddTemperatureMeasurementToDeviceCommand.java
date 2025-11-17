package com.alpacaflow.meditrackplatform.devices.domain.model.commands;

/**
 * Command to add a temperature measurement to a device
 * @param celsius The temperature in celsius
 * @param deviceId The device ID
 */
public record AddTemperatureMeasurementToDeviceCommand(double celsius, Long deviceId) {
    public AddTemperatureMeasurementToDeviceCommand {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
    }
}

