package com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources;

/**
 * Resource for adding a temperature measurement to a device
 * @param celsius The temperature in celsius
 */
public record AddTemperatureMeasurementToDeviceResource(double celsius) {
}

