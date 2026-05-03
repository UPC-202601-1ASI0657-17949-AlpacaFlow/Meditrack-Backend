package com.alpacaflow.meditrackplatform.devices.domain.services;

import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Device;
import com.alpacaflow.meditrackplatform.devices.domain.model.commands.*;

import java.util.Optional;

/**
 * DeviceCommandService
 * Service that handles device commands
 */
public interface DeviceCommandService {
    /**
     * Handle a create device command
     * @param command The create device command containing the device data
     * @return The ID of the created device
     * @see CreateDeviceCommand
     */
    Long handle(CreateDeviceCommand command);

    /**
     * Handle an add heart rate measurement to device command
     * @param command The command containing the measurement data
     * @return The updated device
     * @see AddHeartRateMeasurementToDeviceCommand
     */
    Optional<Device> handle(AddHeartRateMeasurementToDeviceCommand command);

    /**
     * Handle an add temperature measurement to device command
     * @param command The command containing the measurement data
     * @return The updated device
     * @see AddTemperatureMeasurementToDeviceCommand
     */
    Optional<Device> handle(AddTemperatureMeasurementToDeviceCommand command);

    /**
     * Handle an add oxygen measurement to device command
     * @param command The command containing the measurement data
     * @return The updated device
     * @see AddOxygenMeasurementToDeviceCommand
     */
    Optional<Device> handle(AddOxygenMeasurementToDeviceCommand command);
}

