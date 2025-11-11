package com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.devices.domain.model.commands.AddOxygenMeasurementToDeviceCommand;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.AddOxygenMeasurementToDeviceResource;

/**
 * Assembler to convert AddOxygenMeasurementToDeviceResource to AddOxygenMeasurementToDeviceCommand
 */
public class AddOxygenMeasurementToDeviceCommandFromResourceAssembler {
    /**
     * Convert a resource to a command
     * @param resource The resource to convert
     * @param deviceId The device ID
     * @return The AddOxygenMeasurementToDeviceCommand
     */
    public static AddOxygenMeasurementToDeviceCommand toCommandFromResource(
            AddOxygenMeasurementToDeviceResource resource, Long deviceId) {
        return new AddOxygenMeasurementToDeviceCommand(resource.spo2(), deviceId);
    }
}

