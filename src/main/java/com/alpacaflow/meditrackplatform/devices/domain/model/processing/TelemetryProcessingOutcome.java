package com.alpacaflow.meditrackplatform.devices.domain.model.processing;

import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.EMeasurementType;

/**
 * Result of evaluating one telemetry sample against the patient's vital baseline in the devices context.
 * <p>Represents the devices-side processing step: raw metric in, signal classification out. Persistence
 * of {@link com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Alert} remains a separate step.</p>
 */
public record TelemetryProcessingOutcome(
        boolean triggersAlert,
        EMeasurementType measurementType,
        double value
) {
}
