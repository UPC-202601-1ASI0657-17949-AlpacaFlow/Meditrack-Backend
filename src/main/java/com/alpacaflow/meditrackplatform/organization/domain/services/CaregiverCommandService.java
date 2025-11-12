package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Caregiver;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.*;

import java.util.Optional;

/**
 * CaregiverCommandService
 * Service that handles caregiver commands
 */
public interface CaregiverCommandService {
    /**
     * Handle a create caregiver command
     * @param command The create caregiver command containing the caregiver data
     * @return The created caregiver id
     * @see CreateCaregiverCommand
     */
    Long handle(CreateCaregiverCommand command);

    /**
     * Handle an update caregiver command
     * @param command The update caregiver command containing the caregiver data
     * @return The updated caregiver
     * @see UpdateCaregiverCommand
     */
    Optional<Caregiver> handle(UpdateCaregiverCommand command);

    /**
     * Handle a delete caregiver command
     * @param command The delete caregiver command containing the caregiver id
     * @see DeleteCaregiverCommand
     */
    void handle(DeleteCaregiverCommand command);

    /**
     * Handle an assign senior citizen to caregiver command
     * @param command The assign senior citizen to caregiver command containing the caregiver id and senior citizen id
     * @return The caregiver with the assigned senior citizen
     * @see AssignSeniorCitizenToCaregiverCommand
     */
    Optional<Caregiver> handle(AssignSeniorCitizenToCaregiverCommand command);

    /**
     * Handle an unassign senior citizen from caregiver command
     * @param command The unassign senior citizen from caregiver command containing the caregiver id and senior citizen id
     * @return The caregiver with the unassigned senior citizen
     * @see UnassignSeniorCitizenFromCaregiverCommand
     */
    Optional<Caregiver> handle(UnassignSeniorCitizenFromCaregiverCommand command);
}

