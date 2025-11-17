package com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources;

/**
 * Resource for adding a blood pressure measurement to a device
 * @param diastolic The diastolic pressure value
 * @param systolic The systolic pressure value
 */
public record AddBloodPressureMeasurementToDeviceResource(int diastolic, int systolic) {
}

