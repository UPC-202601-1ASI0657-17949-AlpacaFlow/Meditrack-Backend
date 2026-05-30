package com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources;

public record MedicalRecordResource(
        Long id,
        String medicalRecordNumber,
        Long seniorCitizenId,
        String medicalHistoryDescription,
        String allergies
) {}