package com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.devices.domain.model.entities.TemperatureMeasurement;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.TemperatureMeasurementResource;

/**
 * Assembler to convert TemperatureMeasurement entity to TemperatureMeasurementResource
 */
public class TemperatureMeasurementResourceFromEntityAssembler {
    /**
     * Convert a TemperatureMeasurement entity to a TemperatureMeasurementResource
     * @param entity The entity to convert
     * @return The TemperatureMeasurementResource
     */
    public static TemperatureMeasurementResource toResourceFromEntity(TemperatureMeasurement entity) {
        return new TemperatureMeasurementResource(
                entity.getId(),
                entity.getCelsius(),
                entity.getMeasuredAt().toString()
        );
    }
}

