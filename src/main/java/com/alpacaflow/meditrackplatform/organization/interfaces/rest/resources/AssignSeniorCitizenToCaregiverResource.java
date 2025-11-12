package com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources;

/**
 * Assign senior citizen to caregiver resource.
 */
public record AssignSeniorCitizenToCaregiverResource(
        Long seniorCitizenId,
        Long caregiverId
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any of the required fields is null or invalid.
     */
    public AssignSeniorCitizenToCaregiverResource {
        if (seniorCitizenId == null || seniorCitizenId <= 0) {
            throw new IllegalArgumentException("seniorCitizenId is required and must be greater than 0");
        }
        if (caregiverId == null || caregiverId <= 0) {
            throw new IllegalArgumentException("caregiverId is required and must be greater than 0");
        }
    }
}

