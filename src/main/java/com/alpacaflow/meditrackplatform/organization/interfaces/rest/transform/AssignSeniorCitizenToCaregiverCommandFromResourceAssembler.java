package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.AssignSeniorCitizenToCaregiverCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.AssignSeniorCitizenToCaregiverResource;

/**
 * Assembler to convert an AssignSeniorCitizenToCaregiverResource to an AssignSeniorCitizenToCaregiverCommand.
 */
public class AssignSeniorCitizenToCaregiverCommandFromResourceAssembler {
    /**
     * Converts an AssignSeniorCitizenToCaregiverResource to an AssignSeniorCitizenToCaregiverCommand.
     *
     * @param resource The {@link AssignSeniorCitizenToCaregiverResource} resource to convert.
     * @return The {@link AssignSeniorCitizenToCaregiverCommand} command that results from the conversion.
     */
    public static AssignSeniorCitizenToCaregiverCommand toCommandFromResource(AssignSeniorCitizenToCaregiverResource resource) {
        return new AssignSeniorCitizenToCaregiverCommand(
                resource.seniorCitizenId(),
                resource.caregiverId()
        );
    }
}

