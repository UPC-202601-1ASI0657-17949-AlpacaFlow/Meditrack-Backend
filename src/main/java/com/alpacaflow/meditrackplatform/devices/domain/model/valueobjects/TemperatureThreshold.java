package com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing temperature threshold
 */
@Embeddable
public record TemperatureThreshold(double min, double max) {
    public TemperatureThreshold() {
        this(35.0, 38.0);
    }

    public TemperatureThreshold {
        if (min >= max) {
            throw new IllegalArgumentException("Minimum threshold cannot be greater than or equal to maximum threshold");
        }
    }

    /**
     * Determines if a value violates the threshold
     * @param value The value to check
     * @return true if the value is outside the safe range
     */
    public boolean isViolatedBy(double value) {
        return value < min || value > max;
    }
}

