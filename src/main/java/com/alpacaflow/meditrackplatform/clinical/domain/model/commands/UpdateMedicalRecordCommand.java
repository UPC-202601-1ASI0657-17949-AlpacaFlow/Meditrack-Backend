package com.alpacaflow.meditrackplatform.clinical.domain.model.commands;

public record UpdateMedicalRecordCommand(
        Long seniorCitizenId,
        String medicalHistoryDescription,
        String allergies
) {}