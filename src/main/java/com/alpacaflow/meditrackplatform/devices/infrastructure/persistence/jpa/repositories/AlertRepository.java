package com.alpacaflow.meditrackplatform.devices.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Alert aggregate
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
}

