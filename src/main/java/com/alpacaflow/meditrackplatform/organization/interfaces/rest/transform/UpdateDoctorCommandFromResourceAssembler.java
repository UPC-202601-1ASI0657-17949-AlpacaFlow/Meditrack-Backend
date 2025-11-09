package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.UpdateDoctorCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpdateDoctorResource;

/**
 * Assembler to convert an UpdateDoctorResource to an UpdateDoctorCommand.
 */
public class UpdateDoctorCommandFromResourceAssembler {
    /**
     * Converts an UpdateDoctorResource to an UpdateDoctorCommand.
     *
     * @param resource The {@link UpdateDoctorResource} resource to convert.
     * @param doctorId The doctor ID to update.
     * @return The {@link UpdateDoctorCommand} command that results from the conversion.
     */
    public static UpdateDoctorCommand toCommandFromResource(UpdateDoctorResource resource, Long doctorId) {
        return new UpdateDoctorCommand(
                doctorId,
                resource.firstName(),
                resource.lastName(),
                resource.specialty(),
                resource.age(),
                resource.email(),
                resource.phoneNumber(),
                resource.imageUrl()
        );
    }
}

