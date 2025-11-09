package com.alpacaflow.meditrackplatform.iam.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.iam.domain.model.commands.CreateMockUserCommand;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources.CreateMockUserResource;

/**
 * Assembler to convert a CreateMockUserResource to a CreateMockUserCommand.
 */
public class CreateMockUserCommandFromResourceAssembler {
    /**
     * Converts a CreateMockUserResource to a CreateMockUserCommand.
     *
     * @param resource The {@link CreateMockUserResource} resource to convert.
     * @return The {@link CreateMockUserCommand} command that results from the conversion.
     */
    public static CreateMockUserCommand toCommandFromResource(CreateMockUserResource resource) {
        return new CreateMockUserCommand(
                resource.email(),
                resource.role()
        );
    }
}

