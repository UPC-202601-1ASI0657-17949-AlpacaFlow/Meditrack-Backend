package com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources;

/**
 * Resource for adding an oxygen saturation measurement to a device
 * @param spo2 The oxygen saturation percentage
 */
public record AddOxygenMeasurementToDeviceResource(int spo2) {
}

