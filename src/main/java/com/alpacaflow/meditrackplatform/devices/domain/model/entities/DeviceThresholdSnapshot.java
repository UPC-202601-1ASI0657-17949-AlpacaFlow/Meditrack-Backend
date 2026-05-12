package com.alpacaflow.meditrackplatform.devices.domain.model.entities;

import com.alpacaflow.meditrackplatform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Local copy of vital thresholds for hot-path telemetry evaluation (synced from Organization).
 */
@Getter
@Setter
@Entity
@Table(name = "device_threshold_snapshot")
public class DeviceThresholdSnapshot extends AuditableModel {

    @Column(name = "device_id", nullable = false, unique = true)
    private Long deviceId;

    @Column(name = "heart_rate_min", nullable = false)
    private int heartRateMin;

    @Column(name = "heart_rate_max", nullable = false)
    private int heartRateMax;

    @Column(name = "oxygen_saturation_min", nullable = false)
    private double oxygenSaturationMin;

    @Column(name = "temperature_min", nullable = false)
    private double temperatureMin;

    @Column(name = "temperature_max", nullable = false)
    private double temperatureMax;

    public DeviceThresholdSnapshot() {
    }

    public DeviceThresholdSnapshot(
            Long deviceId,
            int heartRateMin,
            int heartRateMax,
            double oxygenSaturationMin,
            double temperatureMin,
            double temperatureMax
    ) {
        this.deviceId = deviceId;
        this.heartRateMin = heartRateMin;
        this.heartRateMax = heartRateMax;
        this.oxygenSaturationMin = oxygenSaturationMin;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
    }
}
