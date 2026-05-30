package com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.CreatePatientThresholdCommand;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.CreatePatientThresholdResource;

public class CreatePatientThresholdCommandFromResourceAssembler {
    public static CreatePatientThresholdCommand toCommandFromResource(CreatePatientThresholdResource resource) {
        return new CreatePatientThresholdCommand(resource.seniorCitizenId());
    }
}