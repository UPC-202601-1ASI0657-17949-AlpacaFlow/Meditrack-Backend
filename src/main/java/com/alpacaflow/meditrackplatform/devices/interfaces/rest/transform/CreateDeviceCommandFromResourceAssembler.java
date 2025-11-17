package com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.devices.domain.model.commands.CreateDeviceCommand;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.CreateDeviceResource;

/**
 * Assembler to convert CreateDeviceResource to CreateDeviceCommand
 */
public class CreateDeviceCommandFromResourceAssembler {
    /**
     * Convert a CreateDeviceResource to a CreateDeviceCommand
     * @param resource The resource to convert
     * @return The CreateDeviceCommand
     */
    public static CreateDeviceCommand toCommandFromResource(CreateDeviceResource resource) {
        return new CreateDeviceCommand(resource.model(), resource.holderId());
    }
}

