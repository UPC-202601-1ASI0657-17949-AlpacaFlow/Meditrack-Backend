package com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.devices.domain.model.entities.HeartRateMeasurement;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.HeartRateMeasurementResource;

/**
 * Assembler to convert HeartRateMeasurement entity to HeartRateMeasurementResource
 */
public class HeartRateMeasurementResourceFromEntityAssembler {
    /**
     * Convert a HeartRateMeasurement entity to a HeartRateMeasurementResource
     * @param entity The entity to convert
     * @return The HeartRateMeasurementResource
     */
    public static HeartRateMeasurementResource toResourceFromEntity(HeartRateMeasurement entity) {
        return new HeartRateMeasurementResource(
                entity.getId(),
                entity.getBpm(),
                entity.getMeasuredAt().toString()
        );
    }
}

