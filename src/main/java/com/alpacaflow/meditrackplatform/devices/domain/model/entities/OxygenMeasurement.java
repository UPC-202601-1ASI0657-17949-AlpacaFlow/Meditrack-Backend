package com.alpacaflow.meditrackplatform.devices.domain.model.entities;

import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.OxygenThreshold;
import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.EMeasurementType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing an oxygen saturation measurement
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("OXYGEN")
public class OxygenMeasurement extends Measurement {
    private int spo2;

    @Embedded
    private OxygenThreshold threshold;

    public OxygenMeasurement() {
        super(EMeasurementType.OXYGEN);
        this.threshold = new OxygenThreshold();
    }

    public OxygenMeasurement(int spo2) {
        super(EMeasurementType.OXYGEN);
        this.spo2 = spo2;
        this.threshold = new OxygenThreshold();
    }

    /**
     * Check if value surpasses the threshold
     * @return true if value is below safe minimum
     */
    public boolean surpassesThreshold() {
        return threshold.isViolatedBy(spo2);
    }

    public int getSpo2() {
        return spo2;
    }
}

