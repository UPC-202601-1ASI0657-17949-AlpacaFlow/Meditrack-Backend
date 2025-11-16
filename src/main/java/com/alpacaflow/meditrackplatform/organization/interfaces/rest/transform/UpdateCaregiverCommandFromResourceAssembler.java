package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.UpdateCaregiverCommand;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpdateCaregiverResource;

/**
 * Assembler to convert an UpdateCaregiverResource to an UpdateCaregiverCommand.
 */
public class UpdateCaregiverCommandFromResourceAssembler {
    /**
     * Converts an UpdateCaregiverResource to an UpdateCaregiverCommand.
     *
     * @param resource The {@link UpdateCaregiverResource} resource to convert.
     * @param caregiverId The caregiver ID to update.
     * @return The {@link UpdateCaregiverCommand} command that results from the conversion.
     */
    public static UpdateCaregiverCommand toCommandFromResource(UpdateCaregiverResource resource, Long caregiverId) {
        return new UpdateCaregiverCommand(
                caregiverId,
                resource.firstName(),
                resource.lastName(),
                resource.age(),
                resource.email(),
                resource.phoneNumber(),
                resource.imageUrl()
        );
    }
}

