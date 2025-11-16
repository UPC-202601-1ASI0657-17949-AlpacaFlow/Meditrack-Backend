package com.alpacaflow.meditrackplatform.relatives.interfaces.rest.resources;

public record SeniorCitizenResource(
    Long id,
    String firstName,
    String lastName,
    String dni,
    String gender,
    Float height,
    String birthDate,
    Float weight,
    String profileImage,
    String deviceId
) {
}
