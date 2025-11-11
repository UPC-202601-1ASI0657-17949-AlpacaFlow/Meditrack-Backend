package com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.devices.domain.model.commands.AddBloodPressureMeasurementToDeviceCommand;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.AddBloodPressureMeasurementToDeviceResource;

/**
 * Assembler to convert AddBloodPressureMeasurementToDeviceResource to AddBloodPressureMeasurementToDeviceCommand
 */
public class AddBloodPressureMeasurementToDeviceCommandFromResourceAssembler {
    /**
     * Convert a resource to a command
     * @param resource The resource to convert
     * @param deviceId The device ID
     * @return The AddBloodPressureMeasurementToDeviceCommand
     */
    public static AddBloodPressureMeasurementToDeviceCommand toCommandFromResource(
            AddBloodPressureMeasurementToDeviceResource resource, Long deviceId) {
        return new AddBloodPressureMeasurementToDeviceCommand(
                resource.diastolic(), resource.systolic(), deviceId);
    }
}

