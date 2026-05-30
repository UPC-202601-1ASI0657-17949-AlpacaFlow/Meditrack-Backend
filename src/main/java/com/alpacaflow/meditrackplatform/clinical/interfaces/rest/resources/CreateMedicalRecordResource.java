package com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources;

public record CreateMedicalRecordResource(
        Long seniorCitizenId,
        String medicalHistoryDescription,
        String allergies
) {}