package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.SeniorCitizen;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.*;

import java.util.Optional;

/**
 * SeniorCitizenCommandService
 * Service that handles senior citizen commands
 */
public interface SeniorCitizenCommandService {
    /**
     * Handle a create senior citizen command
     * @param command The create senior citizen command containing the senior citizen data
     * @return The created senior citizen id
     * @see CreateSeniorCitizenCommand
     */
    Long handle(CreateSeniorCitizenCommand command);

    /**
     * Handle an update senior citizen command
     * @param command The update senior citizen command containing the senior citizen data
     * @return The updated senior citizen
     * @see UpdateSeniorCitizenCommand
     */
    Optional<SeniorCitizen> handle(UpdateSeniorCitizenCommand command);

    /**
     * Handle a delete senior citizen command
     * @param command The delete senior citizen command containing the senior citizen id
     * @see DeleteSeniorCitizenCommand
     */
    void handle(DeleteSeniorCitizenCommand command);

    /**
     * Handle an assign senior citizen to doctor command
     * @param command The assign senior citizen to doctor command containing the senior citizen id and doctor id
     * @return The senior citizen with the assigned doctor
     * @see AssignSeniorCitizenToDoctorCommand
     */
    Optional<SeniorCitizen> handle(AssignSeniorCitizenToDoctorCommand command);

    /**
     * Handle an unassign senior citizen from doctor command
     * @param command The unassign senior citizen from doctor command containing the senior citizen id and doctor id
     * @return The senior citizen with the unassigned doctor
     * @see UnassignSeniorCitizenFromDoctorCommand
     */
    Optional<SeniorCitizen> handle(UnassignSeniorCitizenFromDoctorCommand command);

    /**
     * Handle an assign senior citizen to caregiver command
     * @param command The assign senior citizen to caregiver command containing the senior citizen id and caregiver id
     * @return The senior citizen with the assigned caregiver
     * @see AssignSeniorCitizenToCaregiverCommand
     */
    Optional<SeniorCitizen> handle(AssignSeniorCitizenToCaregiverCommand command);

    /**
     * Handle an unassign senior citizen from caregiver command
     * @param command The unassign senior citizen from caregiver command containing the senior citizen id and caregiver id
     * @return The senior citizen with the unassigned caregiver
     * @see UnassignSeniorCitizenFromCaregiverCommand
     */
    Optional<SeniorCitizen> handle(UnassignSeniorCitizenFromCaregiverCommand command);
}

