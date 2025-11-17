package com.alpacaflow.meditrackplatform.devices.domain.model.entities;

import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.TemperatureThreshold;
import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.EMeasurementType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a temperature measurement
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("TEMPERATURE")
public class TemperatureMeasurement extends Measurement {
    private double celsius;

    @Embedded
    private TemperatureThreshold threshold;

    public TemperatureMeasurement() {
        super(EMeasurementType.TEMPERATURE);
        this.threshold = new TemperatureThreshold();
    }

    public TemperatureMeasurement(double celsius) {
        super(EMeasurementType.TEMPERATURE);
        this.celsius = celsius;
        this.threshold = new TemperatureThreshold();
    }

    /**
     * Check if value surpasses the threshold
     * @return true if value is outside safe range
     */
    public boolean surpassesThreshold() {
        return threshold.isViolatedBy(celsius);
    }

    public double getCelsius() {
        return celsius;
    }
}

