package com.alpacaflow.meditrackplatform.clinical.domain.model.commands;

public record CreatePatientThresholdCommand(Long seniorCitizenId) {
    public CreatePatientThresholdCommand {
        if (seniorCitizenId == null) {
            throw new IllegalArgumentException("El ID del adulto mayor no puede ser nulo");
        }
    }
}