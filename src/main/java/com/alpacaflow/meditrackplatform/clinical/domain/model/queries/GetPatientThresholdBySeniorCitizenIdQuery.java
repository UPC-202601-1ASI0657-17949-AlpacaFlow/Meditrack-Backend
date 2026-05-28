package com.alpacaflow.meditrackplatform.clinical.domain.model.queries;

public record GetPatientThresholdBySeniorCitizenIdQuery(Long seniorCitizenId) {
    public GetPatientThresholdBySeniorCitizenIdQuery {
        if (seniorCitizenId == null) {
            throw new IllegalArgumentException("El ID del adulto mayor no puede ser nulo");
        }
    }
}