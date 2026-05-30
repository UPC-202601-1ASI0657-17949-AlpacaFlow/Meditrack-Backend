package com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources;

public record UpdateMedicalRecordResource(
        String medicalHistoryDescription,
        String allergies
) {}