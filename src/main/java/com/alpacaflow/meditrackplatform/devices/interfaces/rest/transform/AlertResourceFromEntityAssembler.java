package com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Alert;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.AlertResource;

/**
 * Assembler to convert Alert entity to AlertResource
 */
public class AlertResourceFromEntityAssembler {
    /**
     * Convert an Alert entity to an AlertResource
     * @param entity The entity to convert
     * @return The AlertResource
     */
    public static AlertResource toResourceFromEntity(Alert entity) {
        return new AlertResource(
                entity.getId(),
                entity.getDeviceId(),
                entity.getAlertType().toString(),
                entity.getMessage(),
                entity.getDataRegistered(),
                entity.getRegisteredAt().toString()
        );
    }
}

