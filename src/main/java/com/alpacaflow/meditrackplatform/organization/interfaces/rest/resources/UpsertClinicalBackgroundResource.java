package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

import com.alpacaflow.meditrackplatform.organization.domain.model.entities.ClinicalBackground;
import com.alpacaflow.meditrackplatform.organization.domain.model.valueobjects.ClinicalBackgroundAuthorRole;
import jakarta.validation.constraints.Size;

/**
 * Request body for upserting clinical background (Organization or Relative flows).
 */
public record UpsertClinicalBackgroundResource(
        boolean hypertension,
        boolean diabetes,
        boolean cardiovascularDisease,
        boolean respiratoryDisease,
        @Size(max = ClinicalBackground.MAX_TEXT_FIELD_LENGTH) String allergies,
        @Size(max = ClinicalBackground.MAX_TEXT_FIELD_LENGTH) String medications,
        @Size(max = ClinicalBackground.MAX_TEXT_FIELD_LENGTH) String mobilityNotes,
        @Size(max = ClinicalBackground.MAX_TEXT_FIELD_LENGTH) String cognitiveCondition,
        @Size(max = ClinicalBackground.MAX_TEXT_FIELD_LENGTH) String generalNotes,
        ClinicalBackgroundAuthorRole authorRole,
        Long authorId
) {
}
