package com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.devices.domain.model.entities.BloodPressureMeasurement;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.BloodPressureMeasurementResource;

/**
 * Assembler to convert BloodPressureMeasurement entity to BloodPressureMeasurementResource
 */
public class BloodPressureMeasurementResourceFromEntityAssembler {
    /**
     * Convert a BloodPressureMeasurement entity to a BloodPressureMeasurementResource
     * @param entity The entity to convert
     * @return The BloodPressureMeasurementResource
     */
    public static BloodPressureMeasurementResource toResourceFromEntity(BloodPressureMeasurement entity) {
        return new BloodPressureMeasurementResource(
                entity.getId(),
                entity.getDiastolic(),
                entity.getSystolic(),
                entity.getMeasuredAt().toString()
        );
    }
}

