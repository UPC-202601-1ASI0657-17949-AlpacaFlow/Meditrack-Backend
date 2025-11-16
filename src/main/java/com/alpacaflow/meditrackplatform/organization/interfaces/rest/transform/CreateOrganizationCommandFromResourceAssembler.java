package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.CreateOrganizationCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CreateOrganizationResource;

/**
 * Assembler to convert a CreateOrganizationResource to a CreateOrganizationCommand.
 */
public class CreateOrganizationCommandFromResourceAssembler {
    /**
     * Converts a CreateOrganizationResource to a CreateOrganizationCommand.
     *
     * @param resource The {@link CreateOrganizationResource} resource to convert.
     * @return The {@link CreateOrganizationCommand} command that results from the conversion.
     */
    public static CreateOrganizationCommand toCommandFromResource(CreateOrganizationResource resource) {
        return new CreateOrganizationCommand(
                resource.name(),
                resource.type()
        );
    }
}

