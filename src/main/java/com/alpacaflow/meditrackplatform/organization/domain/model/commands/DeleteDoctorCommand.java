package com.alpacaflow.meditrackplatform.organization.domain.model.commands;

/**
 * Command to delete a doctor.
 */
public record DeleteDoctorCommand(Long doctorId) {
    public DeleteDoctorCommand {
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("doctorId cannot be null or less than 1");
        }
    }
}

