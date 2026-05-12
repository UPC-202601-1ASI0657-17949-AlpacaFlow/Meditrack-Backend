package com.alpacaflow.meditrackplatform.organization.domain.model.valueobjects;

/**
 * Who first registered the clinical background row (audit only).
 * Updates do not change this; {@code updatedAt} reflects last edit.
 */
public enum ClinicalBackgroundAuthorRole {
    ORGANIZATION,
    RELATIVE
}
