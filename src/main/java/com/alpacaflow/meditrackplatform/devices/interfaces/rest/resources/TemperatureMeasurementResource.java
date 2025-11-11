package com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources;

/**
 * Resource representing a temperature measurement
 */
public record TemperatureMeasurementResource(
        Long id,
        double celsius,
        String measuredAt
) {
}

