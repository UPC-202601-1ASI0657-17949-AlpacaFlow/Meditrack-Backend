package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

import com.alpacaflow.meditrackplatform.organization.domain.model.valueobjects.ClinicalBackgroundAuthorRole;

/**
 * Response DTO for descriptive clinical background (not used by Monitoring for alerts).
 */
public record ClinicalBackgroundResource(
        Long id,
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
        ClinicalBackgroundAuthorRole createdByRole,
        Long createdById,
        Long createdAtEpochMillis,
        Long updatedAtEpochMillis
) {
}
