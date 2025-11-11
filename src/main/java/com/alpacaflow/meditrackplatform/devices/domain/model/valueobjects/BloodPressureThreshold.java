package com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing blood pressure threshold
 */
@Embeddable
public record BloodPressureThreshold(int min, int max) {
    public BloodPressureThreshold() {
        this(90, 180);
    }

    public BloodPressureThreshold {
        if (min >= max) {
            throw new IllegalArgumentException("Minimum threshold cannot be greater than or equal to maximum threshold");
        }
    }

    /**
     * Determines if a value violates the threshold
     * @param value The value to check
     * @return true if the value is outside the safe range
     */
    public boolean isViolatedBy(int value) {
        return value < min || value > max;
    }
}

