package com.alpacaflow.meditrackplatform.clinical.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record TemperatureThreshold(double minCelsius, double maxCelsius) {
    public TemperatureThreshold {
        if (minCelsius >= maxCelsius) {
            throw new IllegalArgumentException("La temperatura mínima debe ser menor que la máxima");
        }
    }

    public static TemperatureThreshold defaultConfig() {
        return new TemperatureThreshold(35.0, 37.5);
    }
}