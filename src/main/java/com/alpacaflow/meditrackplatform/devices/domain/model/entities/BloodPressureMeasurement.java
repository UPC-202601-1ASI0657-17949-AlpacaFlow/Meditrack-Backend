package com.alpacaflow.meditrackplatform.devices.domain.model.entities;

import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.BloodPressureThreshold;
import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.EMeasurementType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a blood pressure measurement
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("BLOOD_PRESSURE")
public class BloodPressureMeasurement extends Measurement {
    private int diastolic;
    private int systolic;

    @Embedded
    private BloodPressureThreshold threshold;

    public BloodPressureMeasurement() {
        super(EMeasurementType.BLOOD_PRESSURE);
        this.threshold = new BloodPressureThreshold();
    }

    public BloodPressureMeasurement(int diastolic, int systolic) {
        super(EMeasurementType.BLOOD_PRESSURE);
        this.diastolic = diastolic;
        this.systolic = systolic;
        this.threshold = new BloodPressureThreshold();
    }

    /**
     * Check if diastolic value surpasses the threshold
     * @return true if diastolic value is outside safe range
     */
    public boolean diastolicSurpassesThreshold() {
        return threshold.isViolatedBy(diastolic);
    }

    /**
     * Check if systolic value surpasses the threshold
     * @return true if systolic value is outside safe range
     */
    public boolean systolicSurpassesThreshold() {
        return threshold.isViolatedBy(systolic);
    }

    public int getDiastolic() {
        return diastolic;
    }

    public int getSystolic() {
        return systolic;
    }
}

