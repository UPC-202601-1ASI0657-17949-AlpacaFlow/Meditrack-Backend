package com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates;

import com.alpacaflow.meditrackplatform.clinical.domain.model.valueobjects.HeartRateThreshold;
import com.alpacaflow.meditrackplatform.clinical.domain.model.valueobjects.OxygenThreshold;
import com.alpacaflow.meditrackplatform.clinical.domain.model.valueobjects.TemperatureThreshold;
import com.alpacaflow.meditrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "patient_thresholds")
public class PatientThreshold extends AuditableAbstractAggregateRoot<PatientThreshold> {

    @Column(nullable = false, unique = true)
    private Long seniorCitizenId;

    @Embedded
    private HeartRateThreshold heartRateThreshold;

    @Embedded
    private OxygenThreshold oxygenThreshold;

    @Embedded
    private TemperatureThreshold temperatureThreshold;

    public PatientThreshold() {
        this.seniorCitizenId = null;
        this.heartRateThreshold = null;
        this.oxygenThreshold = null;
        this.temperatureThreshold = null;
    }

    // Constructor para inicializar con umbrales personalizados o por defecto
    public PatientThreshold(Long seniorCitizenId) {
        this.seniorCitizenId = seniorCitizenId;
        this.heartRateThreshold = HeartRateThreshold.defaultConfig();
        this.oxygenThreshold = OxygenThreshold.defaultConfig();
        this.temperatureThreshold = TemperatureThreshold.defaultConfig();
    }

    /**
     * Permite al usuario (médico/familiar) configurar manualmente los umbrales
     */
    public void updateThresholds(HeartRateThreshold heartRateThreshold, OxygenThreshold oxygenThreshold, TemperatureThreshold temperatureThreshold) {
        this.heartRateThreshold = heartRateThreshold;
        this.oxygenThreshold = oxygenThreshold;
        this.temperatureThreshold = temperatureThreshold;
    }

    /**
     * Evalúa si los signos vitales recibidos del parche IoT están fuera de rango
     */
    public boolean isHeartRateAbnormal(int bpm) {
        return bpm < this.heartRateThreshold.minBpm() || bpm > this.heartRateThreshold.maxBpm();
    }

    public boolean isOxygenAbnormal(int spo2) {
        return spo2 < this.oxygenThreshold.minSpo2();
    }

    public boolean isTemperatureAbnormal(double celsius) {
        return celsius < this.temperatureThreshold.minCelsius() || celsius > this.temperatureThreshold.maxCelsius();
    }
}