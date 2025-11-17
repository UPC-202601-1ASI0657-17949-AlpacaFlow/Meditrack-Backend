package com.alpacaflow.meditrackplatform.devices.domain.model.commands;

/**
 * Command to add a blood pressure measurement to a device
 * @param diastolic The diastolic pressure value
 * @param systolic The systolic pressure value
 * @param deviceId The device ID
 */
public record AddBloodPressureMeasurementToDeviceCommand(int diastolic, int systolic, Long deviceId) {
    public AddBloodPressureMeasurementToDeviceCommand {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
    }
}

