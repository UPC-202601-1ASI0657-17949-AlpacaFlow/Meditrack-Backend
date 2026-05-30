package com.alpacaflow.meditrackplatform.clinical.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record HeartRateThreshold(int minBpm, int maxBpm) {
    public HeartRateThreshold {
        if (minBpm < 0 || maxBpm < 0) {
            throw new IllegalArgumentException("Los valores de ritmo cardíaco no pueden ser negativos");
        }
        if (minBpm >= maxBpm) {
            throw new IllegalArgumentException("El umbral mínimo de ritmo cardíaco debe ser menor que el máximo");
        }
    }

    // Valores por defecto para un adulto mayor promedio
    public static HeartRateThreshold defaultConfig() {
        return new HeartRateThreshold(60, 100);
    }
}