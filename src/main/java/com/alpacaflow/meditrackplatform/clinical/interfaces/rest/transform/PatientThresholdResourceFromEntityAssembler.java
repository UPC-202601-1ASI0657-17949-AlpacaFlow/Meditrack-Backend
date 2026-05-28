package com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates.PatientThreshold;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.PatientThresholdResource;

public class PatientThresholdResourceFromEntityAssembler {
    public static PatientThresholdResource toResourceFromEntity(PatientThreshold entity) {
        return new PatientThresholdResource(
                entity.getId(),
                entity.getSeniorCitizenId(),
                entity.getHeartRateThreshold().minBpm(),
                entity.getHeartRateThreshold().maxBpm(),
                entity.getOxygenThreshold().minSpo2(),
                entity.getTemperatureThreshold().minCelsius(),
                entity.getTemperatureThreshold().maxCelsius()
        );
    }
}