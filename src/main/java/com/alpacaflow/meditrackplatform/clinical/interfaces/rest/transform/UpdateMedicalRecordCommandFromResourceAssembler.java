package com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.UpdateMedicalRecordCommand;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.UpdateMedicalRecordResource;

public class UpdateMedicalRecordCommandFromResourceAssembler {
    public static UpdateMedicalRecordCommand toCommandFromResource(Long seniorCitizenId, UpdateMedicalRecordResource resource) {
        return new UpdateMedicalRecordCommand(
                seniorCitizenId,
                resource.medicalHistoryDescription(),
                resource.allergies()
        );
    }
}