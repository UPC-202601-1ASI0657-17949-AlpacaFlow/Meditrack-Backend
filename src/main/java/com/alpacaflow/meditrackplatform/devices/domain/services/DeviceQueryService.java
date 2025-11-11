package com.alpacaflow.meditrackplatform.devices.domain.services;

import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Device;
import com.alpacaflow.meditrackplatform.devices.domain.model.entities.*;
import com.alpacaflow.meditrackplatform.devices.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * DeviceQueryService
 * Service that handles device queries
 */
public interface DeviceQueryService {
    /**
     * Handle a get device by id query
     * @param query The query containing the device id
     * @return The device if found
     * @see GetDeviceByIdQuery
     */
    Optional<Device> handle(GetDeviceByIdQuery query);

    /**
     * Handle a get all devices query
     * @param query The query
     * @return List of all devices
     * @see GetAllDevicesQuery
     */
    List<Device> handle(GetAllDevicesQuery query);

    /**
     * Handle a get all blood pressure measurements by device id query
     * @param query The query containing the device id
     * @return List of blood pressure measurements
     * @see GetAllBloodPressureMeasurementsByDeviceIdQuery
     */
    List<BloodPressureMeasurement> handle(GetAllBloodPressureMeasurementsByDeviceIdQuery query);

    /**
     * Handle a get all heart rate measurements by device id query
     * @param query The query containing the device id
     * @return List of heart rate measurements
     * @see GetAllHeartRateMeasurementsByDeviceIdQuery
     */
    List<HeartRateMeasurement> handle(GetAllHeartRateMeasurementsByDeviceIdQuery query);

    /**
     * Handle a get all temperature measurements by device id query
     * @param query The query containing the device id
     * @return List of temperature measurements
     * @see GetAllTemperatureMeasurementsByDeviceIdQuery
     */
    List<TemperatureMeasurement> handle(GetAllTemperatureMeasurementsByDeviceIdQuery query);

    /**
     * Handle a get all oxygen measurements by device id query
     * @param query The query containing the device id
     * @return List of oxygen measurements
     * @see GetAllOxygenMeasurementsByDeviceIdQuery
     */
    List<OxygenMeasurement> handle(GetAllOxygenMeasurementsByDeviceIdQuery query);
}
