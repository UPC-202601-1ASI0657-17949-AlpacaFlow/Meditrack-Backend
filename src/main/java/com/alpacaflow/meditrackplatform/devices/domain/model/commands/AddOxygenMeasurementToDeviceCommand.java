package com.alpacaflow.meditrackplatform.devices.domain.model.commands;

/**
 * Command to add an oxygen saturation measurement to a device
 * @param spo2 The oxygen saturation percentage
 * @param deviceId The device ID
 */
public record AddOxygenMeasurementToDeviceCommand(int spo2, Long deviceId) {
    public AddOxygenMeasurementToDeviceCommand {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
    }
}

