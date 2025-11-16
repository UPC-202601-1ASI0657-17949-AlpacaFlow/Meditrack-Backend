package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.UpdateOrganizationCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpdateOrganizationResource;

/**
 * Assembler to convert an UpdateOrganizationResource to an UpdateOrganizationCommand.
 */
public class UpdateOrganizationCommandFromResourceAssembler {
    /**
     * Converts an UpdateOrganizationResource to an UpdateOrganizationCommand.
     *
     * @param resource The {@link UpdateOrganizationResource} resource to convert.
     * @param organizationId The organization ID to update.
     * @return The {@link UpdateOrganizationCommand} command that results from the conversion.
     */
    public static UpdateOrganizationCommand toCommandFromResource(UpdateOrganizationResource resource, Long organizationId) {
        return new UpdateOrganizationCommand(
                organizationId,
                resource.name(),
                resource.type()
        );
    }
}

