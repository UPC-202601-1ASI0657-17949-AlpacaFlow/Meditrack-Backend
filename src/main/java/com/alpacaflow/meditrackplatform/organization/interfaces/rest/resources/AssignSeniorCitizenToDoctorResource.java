package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

/**
 * Assign senior citizen to doctor resource.
 */
public record AssignSeniorCitizenToDoctorResource(
        Long seniorCitizenId,
        Long doctorId
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any of the required fields is null or invalid.
     */
    public AssignSeniorCitizenToDoctorResource {
        if (seniorCitizenId == null || seniorCitizenId <= 0) {
            throw new IllegalArgumentException("seniorCitizenId is required and must be greater than 0");
        }
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("doctorId is required and must be greater than 0");
        }
    }
}

