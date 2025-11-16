package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to assign a senior citizen to a doctor.
 */
public record AssignSeniorCitizenToDoctorCommand(
        Long seniorCitizenId,
        Long doctorId
) {
    public AssignSeniorCitizenToDoctorCommand {
        if (seniorCitizenId == null || seniorCitizenId <= 0) {
            throw new IllegalArgumentException("seniorCitizenId cannot be null or less than 1");
        }
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("doctorId cannot be null or less than 1");
        }
    }
}
