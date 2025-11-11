package com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing the holder of a device
 * @param holderId The ID of the holder
 * @param holderType The type of holder
 */
@Embeddable
public record Holder(Long holderId, String holderType) {
    public Holder() {
        this(0L, "");
    }
}

