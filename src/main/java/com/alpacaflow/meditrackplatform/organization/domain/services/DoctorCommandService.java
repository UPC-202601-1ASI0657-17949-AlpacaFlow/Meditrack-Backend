package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Doctor;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.*;

import java.util.Optional;

/**
 * DoctorCommandService
 * Service that handles doctor commands
 */
public interface DoctorCommandService {
    /**
     * Handle a create doctor command
     * @param command The create doctor command containing the doctor data
     * @return The created doctor id
     * @see CreateDoctorCommand
     */
    Long handle(CreateDoctorCommand command);

    /**
     * Handle an update doctor command
     * @param command The update doctor command containing the doctor data
     * @return The updated doctor
     * @see UpdateDoctorCommand
     */
    Optional<Doctor> handle(UpdateDoctorCommand command);

    /**
     * Handle a delete doctor command
     * @param command The delete doctor command containing the doctor id
     * @see DeleteDoctorCommand
     */
    void handle(DeleteDoctorCommand command);

    /**
     * Handle an assign senior citizen to doctor command
     * @param command The assign senior citizen to doctor command containing the doctor id and senior citizen id
     * @return The doctor with the assigned senior citizen
     * @see AssignSeniorCitizenToDoctorCommand
     */
    Optional<Doctor> handle(AssignSeniorCitizenToDoctorCommand command);

    /**
     * Handle an unassign senior citizen from doctor command
     * @param command The unassign senior citizen from doctor command containing the doctor id and senior citizen id
     * @return The doctor with the unassigned senior citizen
     * @see UnassignSeniorCitizenFromDoctorCommand
     */
    Optional<Doctor> handle(UnassignSeniorCitizenFromDoctorCommand command);
}

