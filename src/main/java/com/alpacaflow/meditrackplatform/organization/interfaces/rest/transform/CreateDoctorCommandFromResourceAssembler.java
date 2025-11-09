package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.CreateDoctorCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CreateDoctorResource;

/**
 * Assembler to convert a CreateDoctorResource to a CreateDoctorCommand.
 */
public class CreateDoctorCommandFromResourceAssembler {
    /**
     * Converts a CreateDoctorResource to a CreateDoctorCommand.
     *
     * @param resource The {@link CreateDoctorResource} resource to convert.
     * @return The {@link CreateDoctorCommand} command that results from the conversion.
     */
    public static CreateDoctorCommand toCommandFromResource(CreateDoctorResource resource) {
        return new CreateDoctorCommand(
                resource.organizationId(),
                resource.userId(),
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

