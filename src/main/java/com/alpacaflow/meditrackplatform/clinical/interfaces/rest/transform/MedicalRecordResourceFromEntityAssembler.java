package com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates.MedicalRecord;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.MedicalRecordResource;

public class MedicalRecordResourceFromEntityAssembler {
    public static MedicalRecordResource toResourceFromEntity(MedicalRecord entity) {
        return new MedicalRecordResource(
                entity.getId(),
                entity.getMedicalRecordNumber(),
                entity.getSeniorCitizenId(),
                entity.getMedicalHistoryDescription(),
                entity.getAllergies()
        );
    }
}