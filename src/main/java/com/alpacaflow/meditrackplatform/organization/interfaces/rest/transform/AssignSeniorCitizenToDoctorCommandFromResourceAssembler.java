package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.AssignSeniorCitizenToDoctorCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.AssignSeniorCitizenToDoctorResource;

/**
 * Assembler to convert an AssignSeniorCitizenToDoctorResource to an AssignSeniorCitizenToDoctorCommand.
 */
public class AssignSeniorCitizenToDoctorCommandFromResourceAssembler {
    /**
     * Converts an AssignSeniorCitizenToDoctorResource to an AssignSeniorCitizenToDoctorCommand.
     *
     * @param resource The {@link AssignSeniorCitizenToDoctorResource} resource to convert.
     * @return The {@link AssignSeniorCitizenToDoctorCommand} command that results from the conversion.
     */
    public static AssignSeniorCitizenToDoctorCommand toCommandFromResource(AssignSeniorCitizenToDoctorResource resource) {
        return new AssignSeniorCitizenToDoctorCommand(
                resource.seniorCitizenId(),
                resource.doctorId()
        );
    }
}

