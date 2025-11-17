package com.alpacaflow.meditrackplatform.devices.domain.model.commands;

/**
 * Command to add a heart rate measurement to a device
 * @param bpm The heart rate in beats per minute
 * @param deviceId The device ID
 */
public record AddHeartRateMeasurementToDeviceCommand(int bpm, Long deviceId) {
    public AddHeartRateMeasurementToDeviceCommand {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
    }
}

