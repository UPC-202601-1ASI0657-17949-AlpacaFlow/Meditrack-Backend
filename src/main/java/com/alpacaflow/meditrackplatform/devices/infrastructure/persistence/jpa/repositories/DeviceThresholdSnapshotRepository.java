package com.alpacaflow.meditrackplatform.devices.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.devices.domain.model.entities.DeviceThresholdSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceThresholdSnapshotRepository extends JpaRepository<DeviceThresholdSnapshot, Long> {

    Optional<DeviceThresholdSnapshot> findByDeviceId(Long deviceId);
}
