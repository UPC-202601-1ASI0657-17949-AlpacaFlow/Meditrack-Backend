package com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources;

public record PatientThresholdResource(
        Long id,
        Long seniorCitizenId,
        int minBpm,
        int maxBpm,
        int minSpo2,
        double minCelsius,
        double maxCelsius
) {}