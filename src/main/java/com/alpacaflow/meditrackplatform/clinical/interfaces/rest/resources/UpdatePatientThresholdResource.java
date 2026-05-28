package com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources;

public record UpdatePatientThresholdResource(
        int minBpm,
        int maxBpm,
        int minSpo2,
        double minCelsius,
        double maxCelsius
) {}