package com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Device;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.DeviceResource;

/**
 * Assembler to convert Device entity to DeviceResource
 */
public class DeviceResourceFromEntityAssembler {
    /**
     * Convert a Device entity to a DeviceResource
     * @param entity The entity to convert
     * @return The DeviceResource
     */
    public static DeviceResource toResourceFromEntity(Device entity) {
        return new DeviceResource(
                entity.getId(),
                entity.getModel(),
                entity.getStatus().toString(),
                entity.getHolder().holderId(),
                entity.getHolder().holderType()
        );
    }
}

