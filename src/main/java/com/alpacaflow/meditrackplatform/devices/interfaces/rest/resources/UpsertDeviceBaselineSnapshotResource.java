package com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources;

/**
 * Body for {@code PUT /api/v1/internal/devices/{deviceId}/baseline}.
 */
public record UpsertDeviceBaselineSnapshotResource(
        int heartRateMin,
        int heartRateMax,
        double oxygenSaturationMin,
        double temperatureMin,
        double temperatureMax
) {
}
