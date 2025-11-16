package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.UpdateSeniorCitizenCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpdateSeniorCitizenResource;

/**
 * Assembler to convert an UpdateSeniorCitizenResource to an UpdateSeniorCitizenCommand.
 */
public class UpdateSeniorCitizenCommandFromResourceAssembler {
    /**
     * Converts an UpdateSeniorCitizenResource to an UpdateSeniorCitizenCommand.
     *
     * @param resource The {@link UpdateSeniorCitizenResource} resource to convert.
     * @param seniorCitizenId The senior citizen ID to update.
     * @return The {@link UpdateSeniorCitizenCommand} command that results from the conversion.
     */
    public static UpdateSeniorCitizenCommand toCommandFromResource(UpdateSeniorCitizenResource resource, Long seniorCitizenId) {
        return new UpdateSeniorCitizenCommand(
                seniorCitizenId,
                resource.firstName(),
                resource.lastName(),
                resource.birthDate(),
                resource.gender(),
                resource.weight(),
                resource.dni(),
                resource.height(),
                resource.imageUrl(),
                resource.deviceId()
        );
    }
}

