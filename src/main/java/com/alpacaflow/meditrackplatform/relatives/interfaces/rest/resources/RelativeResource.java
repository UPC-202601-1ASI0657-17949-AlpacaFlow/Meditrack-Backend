package com.alpacaflow.meditrackplatform.relatives.interfaces.rest.resources;

import com.alpacaflow.meditrackplatform.relatives.domain.model.valueobjects.PlanType;

public record RelativeResource(
        Long id,
        PlanType planType,
        String firstName,
        String lastName,
        String phoneNumber,
        SeniorCitizenResource seniorCitizen
) {
}
