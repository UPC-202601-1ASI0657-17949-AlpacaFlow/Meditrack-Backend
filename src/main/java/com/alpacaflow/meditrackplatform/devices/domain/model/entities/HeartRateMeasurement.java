package com.alpacaflow.meditrackplatform.devices.domain.model.entities;

import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.HeartRateThreshold;
import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.EMeasurementType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a heart rate measurement
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("HEART_RATE")
public class HeartRateMeasurement extends Measurement {
    private int bpm;

    @Embedded
    private HeartRateThreshold threshold;

    public HeartRateMeasurement() {
        super(EMeasurementType.HEART_RATE);
        this.threshold = new HeartRateThreshold();
    }

    public HeartRateMeasurement(int bpm) {
        super(EMeasurementType.HEART_RATE);
        this.bpm = bpm;
        this.threshold = new HeartRateThreshold();
    }

    /**
     * Check if value surpasses the threshold
     * @return true if value is outside safe range
     */
    public boolean surpassesThreshold() {
        return threshold.isViolatedBy(bpm);
    }

    public int getBpm() {
        return bpm;
    }
}

