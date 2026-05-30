package com.alpacaflow.meditrackplatform.clinical.domain.model.commands;

public record CreateMedicalRecordCommand(
        Long seniorCitizenId,
        String medicalHistoryDescription,
        String allergies
) {}