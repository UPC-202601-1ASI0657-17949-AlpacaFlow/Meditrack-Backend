package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.CreateAdminCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CreateAdminResource;

/**
 * Assembler to convert a CreateAdminResource to a CreateAdminCommand.
 */
public class CreateAdminCommandFromResourceAssembler {
    /**
     * Converts a CreateAdminResource to a CreateAdminCommand.
     *
     * @param resource The {@link CreateAdminResource} resource to convert.
     * @return The {@link CreateAdminCommand} command that results from the conversion.
     */
    public static CreateAdminCommand toCommandFromResource(CreateAdminResource resource) {
        return new CreateAdminCommand(
                resource.organizationId(),
                resource.userId(),
                resource.firstName(),
                resource.lastName()
        );
    }
}

