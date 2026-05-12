package com.alpacaflow.meditrackplatform.devices.domain.model.commands;

/**
 * Upsert local vital threshold snapshot for a device (typically invoked by Organization ACL).
 */
public record UpsertDeviceBaselineSnapshotCommand(
        Long deviceId,
        int heartRateMin,
        int heartRateMax,
        double oxygenSaturationMin,
        double temperatureMin,
        double temperatureMax
) {
}
