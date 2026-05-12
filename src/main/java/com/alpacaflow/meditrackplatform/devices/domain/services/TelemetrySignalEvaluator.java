package com.alpacaflow.meditrackplatform.devices.domain.services;

import com.alpacaflow.meditrackplatform.devices.domain.model.processing.TelemetryProcessingOutcome;
import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.EMeasurementType;
import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.PatientVitalBaselineSnapshot;
import org.springframework.stereotype.Service;

/**
 * Pure domain logic: compares a normalized reading against a baseline snapshot.
 */
@Service
public class TelemetrySignalEvaluator {

    public TelemetryProcessingOutcome assess(
            EMeasurementType type,
            double value,
            PatientVitalBaselineSnapshot baseline
    ) {
        boolean violated = switch (type) {
            case HEART_RATE -> value < baseline.heartRateMin() || value > baseline.heartRateMax();
            case TEMPERATURE -> value < baseline.temperatureMin() || value > baseline.temperatureMax();
            case OXYGEN -> value < baseline.oxygenSaturationMin();
        };
        return new TelemetryProcessingOutcome(violated, type, value);
    }
}
