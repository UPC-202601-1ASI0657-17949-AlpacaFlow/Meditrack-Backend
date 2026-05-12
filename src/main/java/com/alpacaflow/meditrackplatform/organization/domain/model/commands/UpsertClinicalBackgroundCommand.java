package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

import com.alpacaflow.meditrackplatform.organization.domain.model.valueobjects.ClinicalBackgroundAuthorRole;

public record UpsertClinicalBackgroundCommand(
        Long seniorCitizenId,
        boolean hypertension,
        boolean diabetes,
        boolean cardiovascularDisease,
        boolean respiratoryDisease,
        String allergies,
        String medications,
        String mobilityNotes,
        String cognitiveCondition,
        String generalNotes,
        ClinicalBackgroundAuthorRole authorRole,
        Long authorId
) {
}
