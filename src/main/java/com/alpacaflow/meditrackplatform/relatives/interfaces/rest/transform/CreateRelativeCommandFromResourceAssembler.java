package com.alpacaflow.meditrackplatform.relatives.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.relatives.domain.model.commands.CreateRelativeCommand;
import com.alpacaflow.meditrackplatform.relatives.interfaces.rest.resources.CreateRelativeResource;

public class CreateRelativeCommandFromResourceAssembler {
    public static CreateRelativeCommand toCommandFromResource(CreateRelativeResource resource) {
        return new CreateRelativeCommand(
                resource.userId(),
                resource.firstName(),
                resource.lastName(),
                resource.phoneNumber(),
                resource.planType(),
                resource.seniorCitizenId()
        );
    }
}

