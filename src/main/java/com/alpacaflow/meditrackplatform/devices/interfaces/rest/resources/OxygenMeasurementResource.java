package com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources;

/**
 * Resource representing an oxygen saturation measurement
 */
public record OxygenMeasurementResource(
        Long id,
        int spo2,
        String measuredAt
) {
}

