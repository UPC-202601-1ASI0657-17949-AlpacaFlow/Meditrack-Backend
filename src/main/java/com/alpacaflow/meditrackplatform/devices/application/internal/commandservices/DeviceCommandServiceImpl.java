package com.alpacaflow.meditrackplatform.devices.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.devices.domain.exceptions.DeviceNotFoundException;
import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Device;
import com.alpacaflow.meditrackplatform.devices.domain.model.commands.*;
import com.alpacaflow.meditrackplatform.devices.domain.model.entities.*;
import com.alpacaflow.meditrackplatform.devices.domain.model.events.AlertCreatedEvent;
import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.EMeasurementType;
import com.alpacaflow.meditrackplatform.devices.domain.services.DeviceCommandService;
import com.alpacaflow.meditrackplatform.devices.domain.services.PatientVitalBaselineLookup;
import com.alpacaflow.meditrackplatform.devices.domain.services.TelemetrySignalEvaluator;
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
    private final PatientVitalBaselineLookup vitalBaselineLookup;
    private final TelemetrySignalEvaluator telemetrySignalEvaluator;

    public DeviceCommandServiceImpl(
            DeviceRepository deviceRepository,
            PatientVitalBaselineLookup vitalBaselineLookup,
            TelemetrySignalEvaluator telemetrySignalEvaluator
    ) {
        this.deviceRepository = deviceRepository;
        this.vitalBaselineLookup = vitalBaselineLookup;
        this.telemetrySignalEvaluator = telemetrySignalEvaluator;
    }

    @Override
    public Long handle(CreateDeviceCommand command) {
        var device = new Device(command);
        try {
            var savedDevice = deviceRepository.saveAndFlush(device);
            if (savedDevice.getId() == null) {
                throw new IllegalStateException("Device ID is null after save");
            }
            return savedDevice.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving device: %s".formatted(e.getMessage()), e);
        }
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

        var baseline = vitalBaselineLookup.resolveForDevice(device.getId());
        var outcome = telemetrySignalEvaluator.assess(EMeasurementType.HEART_RATE, command.bpm(), baseline);
        if (outcome.triggersAlert()) {
            device.addDomainEvent(new AlertCreatedEvent(device.getId(), command.bpm(), outcome.measurementType().toString()));
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

        var baseline = vitalBaselineLookup.resolveForDevice(device.getId());
        var outcome = telemetrySignalEvaluator.assess(EMeasurementType.TEMPERATURE, command.celsius(), baseline);
        if (outcome.triggersAlert()) {
            device.addDomainEvent(new AlertCreatedEvent(device.getId(), command.celsius(), outcome.measurementType().toString()));
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

        var baseline = vitalBaselineLookup.resolveForDevice(device.getId());
        var outcome = telemetrySignalEvaluator.assess(EMeasurementType.OXYGEN, command.spo2(), baseline);
        if (outcome.triggersAlert()) {
            device.addDomainEvent(new AlertCreatedEvent(device.getId(), command.spo2(), outcome.measurementType().toString()));
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

