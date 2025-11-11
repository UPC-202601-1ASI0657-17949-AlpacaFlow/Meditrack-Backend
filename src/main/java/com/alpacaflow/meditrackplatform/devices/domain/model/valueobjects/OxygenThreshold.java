package com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing oxygen saturation threshold
 */
@Embeddable
public record OxygenThreshold(int min) {
    public OxygenThreshold() {
        this(90);
    }

    /**
     * Determines if a value violates the threshold
     * @param value The value to check
     * @return true if the value is below the safe minimum
     */
    public boolean isViolatedBy(int value) {
        return value < min;
    }
}

