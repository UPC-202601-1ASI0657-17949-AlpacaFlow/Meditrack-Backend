package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.UpsertClinicalBackgroundCommand;
import com.alpacaflow.meditrackplatform.organization.domain.model.entities.ClinicalBackground;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.ClinicalBackgroundResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpsertClinicalBackgroundResource;

public final class ClinicalBackgroundAssembler {

    private ClinicalBackgroundAssembler() {
    }

    public static UpsertClinicalBackgroundCommand toCommand(Long seniorCitizenId, UpsertClinicalBackgroundResource resource) {
        return new UpsertClinicalBackgroundCommand(
                seniorCitizenId,
                resource.hypertension(),
                resource.diabetes(),
                resource.cardiovascularDisease(),
                resource.respiratoryDisease(),
                resource.allergies(),
                resource.medications(),
                resource.mobilityNotes(),
                resource.cognitiveCondition(),
                resource.generalNotes(),
                resource.authorRole(),
                resource.authorId()
        );
    }

    public static ClinicalBackgroundResource toResource(ClinicalBackground entity) {
        return new ClinicalBackgroundResource(
                entity.getId(),
                entity.seniorCitizenId(),
                entity.isHypertension(),
                entity.isDiabetes(),
                entity.isCardiovascularDisease(),
                entity.isRespiratoryDisease(),
                entity.getAllergies(),
                entity.getMedications(),
                entity.getMobilityNotes(),
                entity.getCognitiveCondition(),
                entity.getGeneralNotes(),
                entity.getCreatedByRole(),
                entity.getCreatedById(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().getTime() : null,
                entity.getUpdatedAt() != null ? entity.getUpdatedAt().getTime() : null
        );
    }

    public static ClinicalBackgroundResource emptyShell(Long seniorCitizenId) {
        return new ClinicalBackgroundResource(
                null,
                seniorCitizenId,
                false,
                false,
                false,
                false,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
