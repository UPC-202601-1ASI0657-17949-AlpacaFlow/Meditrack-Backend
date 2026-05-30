package com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.UpdatePatientThresholdCommand;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.UpdatePatientThresholdResource;

public class UpdatePatientThresholdCommandFromResourceAssembler {
    public static UpdatePatientThresholdCommand toCommandFromResource(Long seniorCitizenId, UpdatePatientThresholdResource resource) {
        return new UpdatePatientThresholdCommand(
                seniorCitizenId,
                resource.minBpm(),
                resource.maxBpm(),
                resource.minSpo2(),
                resource.minCelsius(),
                resource.maxCelsius()
        );
    }
}