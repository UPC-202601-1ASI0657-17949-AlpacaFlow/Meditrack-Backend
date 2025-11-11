package com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.devices.domain.model.commands.AddTemperatureMeasurementToDeviceCommand;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.AddTemperatureMeasurementToDeviceResource;

/**
 * Assembler to convert AddTemperatureMeasurementToDeviceResource to AddTemperatureMeasurementToDeviceCommand
 */
public class AddTemperatureMeasurementToDeviceCommandFromResourceAssembler {
    /**
     * Convert a resource to a command
     * @param resource The resource to convert
     * @param deviceId The device ID
     * @return The AddTemperatureMeasurementToDeviceCommand
     */
    public static AddTemperatureMeasurementToDeviceCommand toCommandFromResource(
            AddTemperatureMeasurementToDeviceResource resource, Long deviceId) {
        return new AddTemperatureMeasurementToDeviceCommand(resource.celsius(), deviceId);
    }
}

