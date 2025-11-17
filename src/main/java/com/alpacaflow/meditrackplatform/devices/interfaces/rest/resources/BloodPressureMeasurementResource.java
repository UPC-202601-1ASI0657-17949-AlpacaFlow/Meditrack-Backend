package com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources;

/**
 * Resource representing a blood pressure measurement
 */
public record BloodPressureMeasurementResource(
        Long id,
        int diastolic,
        int systolic,
        String measuredAt
) {
}

