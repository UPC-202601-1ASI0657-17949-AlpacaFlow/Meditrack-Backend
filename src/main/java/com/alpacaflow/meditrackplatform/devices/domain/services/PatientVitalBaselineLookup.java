package com.alpacaflow.meditrackplatform.devices.domain.services;

import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.PatientVitalBaselineSnapshot;

/**
 * Resolves the vital baseline used on the telemetry hot path.
 * <p>Values are materialized as {@link com.alpacaflow.meditrackplatform.devices.domain.model.entities.DeviceThresholdSnapshot}
 * in this context (pushed from Organization), so each BPM does not cross the network to Organization.</p>
 */
public interface PatientVitalBaselineLookup {
    /**
     * @param deviceId persisted {@link com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Device} id
     */
    PatientVitalBaselineSnapshot resolveForDevice(Long deviceId);
}
