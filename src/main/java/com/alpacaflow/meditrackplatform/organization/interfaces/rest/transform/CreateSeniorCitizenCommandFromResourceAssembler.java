package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.CreateSeniorCitizenCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CreateSeniorCitizenResource;

/**
 * Assembler to convert a CreateSeniorCitizenResource to a CreateSeniorCitizenCommand.
 */
public class CreateSeniorCitizenCommandFromResourceAssembler {
    /**
     * Converts a CreateSeniorCitizenResource to a CreateSeniorCitizenCommand.
     *
     * @param resource The {@link CreateSeniorCitizenResource} resource to convert.
     * @return The {@link CreateSeniorCitizenCommand} command that results from the conversion.
     */
    public static CreateSeniorCitizenCommand toCommandFromResource(CreateSeniorCitizenResource resource) {
        return new CreateSeniorCitizenCommand(
                resource.organizationId(),
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

