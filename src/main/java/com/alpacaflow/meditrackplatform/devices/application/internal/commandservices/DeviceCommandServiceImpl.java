package com.alpacaflow.meditrackplatform.devices.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.devices.domain.exceptions.DeviceNotFoundException;
import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Device;
import com.alpacaflow.meditrackplatform.devices.domain.model.commands.*;
import com.alpacaflow.meditrackplatform.devices.domain.model.entities.*;
import com.alpacaflow.meditrackplatform.devices.domain.model.events.AlertCreatedEvent;
import com.alpacaflow.meditrackplatform.devices.domain.services.DeviceCommandService;
import com.alpacaflow.meditrackplatform.devices.infrastructure.persistence.jpa.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the DeviceCommandService interface.
 * <p>This class is responsible for handling the commands related to the Device aggregate. It requires a DeviceRepository.</p>
 * @see DeviceCommandService
 * @see DeviceRepository
 */
@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {
    private final DeviceRepository deviceRepository;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Long handle(CreateDeviceCommand command) {
        var device = new Device(command);
        try {
            deviceRepository.save(device);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving device: %s".formatted(e.getMessage()));
        }
        return device.getId();
    }

    @Override
    public Optional<Device> handle(AddBloodPressureMeasurementToDeviceCommand command) {
        var deviceOptional = deviceRepository.findById(command.deviceId());
        if (deviceOptional.isEmpty()) {
            throw new DeviceNotFoundException(command.deviceId());
        }
        var device = deviceOptional.get();

        if (device.existsMoreThanWeeklyMeasurementsOfType(BloodPressureMeasurement.class)) {
            device.removeLastMeasurementOfType(BloodPressureMeasurement.class);
        }

        var measurement = new BloodPressureMeasurement(command.diastolic(), command.systolic());

        if (measurement.diastolicSurpassesThreshold()) {
            device.addDomainEvent(new AlertCreatedEvent(device.getId(), command.diastolic(), measurement.getType().toString()));
        }
        if (measurement.systolicSurpassesThreshold()) {
            device.addDomainEvent(new AlertCreatedEvent(device.getId(), command.systolic(), measurement.getType().toString()));
        }

        device.addBloodPressure(command.diastolic(), command.systolic());

        try {
            deviceRepository.save(device);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving device: %s".formatted(e.getMessage()));
        }

        return Optional.of(device);
    }

    @Override
    public Optional<Device> handle(AddHeartRateMeasurementToDeviceCommand command) {
        var deviceOptional = deviceRepository.findById(command.deviceId());
        if (deviceOptional.isEmpty()) {
            throw new DeviceNotFoundException(command.deviceId());
        }
        var device = deviceOptional.get();

        if (device.existsMoreThanWeeklyMeasurementsOfType(HeartRateMeasurement.class)) {
            device.removeLastMeasurementOfType(HeartRateMeasurement.class);
        }

        var measurement = new HeartRateMeasurement(command.bpm());

        if (measurement.surpassesThreshold()) {
            device.addDomainEvent(new AlertCreatedEvent(device.getId(), command.bpm(), measurement.getType().toString()));
        }

        device.addHeartRate(command.bpm());

        try {
            deviceRepository.save(device);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving device: %s".formatted(e.getMessage()));
        }

        return Optional.of(device);
    }

    @Override
    public Optional<Device> handle(AddTemperatureMeasurementToDeviceCommand command) {
        var deviceOptional = deviceRepository.findById(command.deviceId());
        if (deviceOptional.isEmpty()) {
            throw new DeviceNotFoundException(command.deviceId());
        }
        var device = deviceOptional.get();

        if (device.existsMoreThanWeeklyMeasurementsOfType(TemperatureMeasurement.class)) {
            device.removeLastMeasurementOfType(TemperatureMeasurement.class);
        }

        var measurement = new TemperatureMeasurement(command.celsius());

        if (measurement.surpassesThreshold()) {
            device.addDomainEvent(new AlertCreatedEvent(device.getId(), command.celsius(), measurement.getType().toString()));
        }

        device.addTemperature(command.celsius());

        try {
            deviceRepository.save(device);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving device: %s".formatted(e.getMessage()));
        }

        return Optional.of(device);
    }

    @Override
    public Optional<Device> handle(AddOxygenMeasurementToDeviceCommand command) {
        var deviceOptional = deviceRepository.findById(command.deviceId());
        if (deviceOptional.isEmpty()) {
            throw new DeviceNotFoundException(command.deviceId());
        }
        var device = deviceOptional.get();

        if (device.existsMoreThanWeeklyMeasurementsOfType(OxygenMeasurement.class)) {
            device.removeLastMeasurementOfType(OxygenMeasurement.class);
        }

        var measurement = new OxygenMeasurement(command.spo2());

        if (measurement.surpassesThreshold()) {
            device.addDomainEvent(new AlertCreatedEvent(device.getId(), command.spo2(), measurement.getType().toString()));
        }

        device.addOxygen(command.spo2());

        try {
            deviceRepository.save(device);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving device: %s".formatted(e.getMessage()));
        }

        return Optional.of(device);
    }
}

