package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.CreateCaregiverCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CreateCaregiverResource;

/**
 * Assembler to convert a CreateCaregiverResource to a CreateCaregiverCommand.
 */
public class CreateCaregiverCommandFromResourceAssembler {
    /**
     * Converts a CreateCaregiverResource to a CreateCaregiverCommand.
     *
     * @param resource The {@link CreateCaregiverResource} resource to convert.
     * @return The {@link CreateCaregiverCommand} command that results from the conversion.
     */
    public static CreateCaregiverCommand toCommandFromResource(CreateCaregiverResource resource) {
        return new CreateCaregiverCommand(
                resource.organizationId(),
                resource.userId(),
                resource.firstName(),
                resource.lastName(),
                resource.age(),
                resource.email(),
                resource.phoneNumber(),
                resource.imageUrl()
        );
    }
}

