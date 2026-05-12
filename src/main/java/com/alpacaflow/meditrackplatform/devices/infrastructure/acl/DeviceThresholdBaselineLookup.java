package com.alpacaflow.meditrackplatform.devices.infrastructure.acl;

import com.alpacaflow.meditrackplatform.devices.domain.model.entities.DeviceThresholdSnapshot;
import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.PatientVitalBaselineSnapshot;
import com.alpacaflow.meditrackplatform.devices.domain.services.PatientVitalBaselineLookup;
import com.alpacaflow.meditrackplatform.devices.infrastructure.persistence.jpa.repositories.DeviceThresholdSnapshotRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Reads persisted per-device threshold snapshot; falls back to system defaults until Organization pushes a row.
 */
@Service
@Primary
public class DeviceThresholdBaselineLookup implements PatientVitalBaselineLookup {

    private final DeviceThresholdSnapshotRepository snapshotRepository;

    public DeviceThresholdBaselineLookup(DeviceThresholdSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    @Override
    public PatientVitalBaselineSnapshot resolveForDevice(Long deviceId) {
        return snapshotRepository.findByDeviceId(deviceId)
                .map(DeviceThresholdBaselineLookup::toSnapshot)
                .orElse(PatientVitalBaselineSnapshot.systemDefaults());
    }

    private static PatientVitalBaselineSnapshot toSnapshot(DeviceThresholdSnapshot e) {
        return new PatientVitalBaselineSnapshot(
                e.getHeartRateMin(),
                e.getHeartRateMax(),
                e.getOxygenSaturationMin(),
                e.getTemperatureMin(),
                e.getTemperatureMax()
        );
    }
}
