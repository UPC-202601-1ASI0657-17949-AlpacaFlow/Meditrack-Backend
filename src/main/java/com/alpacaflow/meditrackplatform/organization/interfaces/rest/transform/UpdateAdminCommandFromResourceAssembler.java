package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.UpdateAdminCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpdateAdminResource;

/**
 * Assembler to convert an UpdateAdminResource to an UpdateAdminCommand.
 */
public class UpdateAdminCommandFromResourceAssembler {
    /**
     * Converts an UpdateAdminResource to an UpdateAdminCommand.
     *
     * @param resource The {@link UpdateAdminResource} resource to convert.
     * @param adminId The admin ID to update.
     * @return The {@link UpdateAdminCommand} command that results from the conversion.
     */
    public static UpdateAdminCommand toCommandFromResource(UpdateAdminResource resource, Long adminId) {
        return new UpdateAdminCommand(
                adminId,
                resource.firstName(),
                resource.lastName()
        );
    }
}

