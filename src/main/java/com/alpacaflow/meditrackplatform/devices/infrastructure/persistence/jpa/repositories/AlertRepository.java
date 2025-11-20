package com.alpacaflow.meditrackplatform.devices.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Alert aggregate
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    /**
     * Find all alerts by device ID
     * @param deviceId The device ID
     * @return List of alerts for the device
     */
    List<Alert> findByDeviceId(Long deviceId);
}

