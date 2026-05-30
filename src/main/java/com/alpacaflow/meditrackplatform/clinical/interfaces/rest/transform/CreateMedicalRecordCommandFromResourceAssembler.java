package com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.CreateMedicalRecordCommand;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.CreateMedicalRecordResource;

public class CreateMedicalRecordCommandFromResourceAssembler {
    public static CreateMedicalRecordCommand toCommandFromResource(CreateMedicalRecordResource resource) {
        return new CreateMedicalRecordCommand(
                resource.seniorCitizenId(),
                resource.medicalHistoryDescription(),
                resource.allergies()
        );
    }
}