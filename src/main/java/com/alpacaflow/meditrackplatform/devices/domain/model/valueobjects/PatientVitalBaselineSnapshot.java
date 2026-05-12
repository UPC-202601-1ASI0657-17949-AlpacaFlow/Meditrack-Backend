package com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects;

/**
 * Immutable thresholds used to classify a single IoT reading as normal vs alert-worthy.
 * <p>Resolved from the Organization bounded context (patient vital baseline). When no patient-specific
 * baseline exists yet, {@link #systemDefaults()} matches the historical embedded defaults on measurements.</p>
 */
public record PatientVitalBaselineSnapshot(
        int heartRateMin,
        int heartRateMax,
        double oxygenSaturationMin,
        double temperatureMin,
        double temperatureMax
) {
    public PatientVitalBaselineSnapshot {
        if (heartRateMin >= heartRateMax) {
            throw new IllegalArgumentException("heartRateMin must be less than heartRateMax");
        }
        if (temperatureMin >= temperatureMax) {
            throw new IllegalArgumentException("temperatureMin must be less than temperatureMax");
        }
        if (oxygenSaturationMin < 70 || oxygenSaturationMin > 100) {
            throw new IllegalArgumentException("oxygenSaturationMin must be between 70 and 100");
        }
    }

    /**
     * Same numeric defaults previously embedded in {@code HeartRateThreshold}, {@code TemperatureThreshold},
     * and {@code OxygenThreshold} on measurement entities.
     */
    public static PatientVitalBaselineSnapshot systemDefaults() {
        return new PatientVitalBaselineSnapshot(50, 120, 90, 35.0, 38.0);
    }
}
