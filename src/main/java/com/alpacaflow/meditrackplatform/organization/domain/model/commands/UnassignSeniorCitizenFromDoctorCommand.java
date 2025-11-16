package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to unassign a senior citizen from a doctor.
 */
public record UnassignSeniorCitizenFromDoctorCommand(
        Long seniorCitizenId,
        Long doctorId
) {
    public UnassignSeniorCitizenFromDoctorCommand {
        if (seniorCitizenId == null || seniorCitizenId <= 0) {
            throw new IllegalArgumentException("seniorCitizenId cannot be null or less than 1");
        }
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("doctorId cannot be null or less than 1");
        }
    }
}
