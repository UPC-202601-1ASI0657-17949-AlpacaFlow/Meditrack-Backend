package com.alpacaflow.meditrackplatform.clinical.domain.model.queries;

public record GetMedicalRecordBySeniorCitizenIdQuery(Long seniorCitizenId) {
    public GetMedicalRecordBySeniorCitizenIdQuery {
        if (seniorCitizenId == null) {
            throw new IllegalArgumentException("El ID del adulto mayor no puede ser nulo");
        }
    }
}