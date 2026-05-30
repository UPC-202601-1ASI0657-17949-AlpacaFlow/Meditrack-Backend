package com.alpacaflow.meditrackplatform.clinical.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record OxygenThreshold(int minSpo2) {
    public OxygenThreshold {
        if (minSpo2 < 0 || minSpo2 > 100) {
            throw new IllegalArgumentException("La saturación de oxígeno debe estar entre 0% y 100%");
        }
    }

    public static OxygenThreshold defaultConfig() {
        return new OxygenThreshold(92); // Menos de 92% suele ser alerta en adultos mayores
    }
}