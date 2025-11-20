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
import java.util.Random;

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
        
        // Generate 7 demo measurements for each type (some inside, some outside safe ranges)
        generateDemoMeasurements(device);
        
        try {
            deviceRepository.save(device);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving device: %s".formatted(e.getMessage()));
        }
        return device.getId();
    }
    
    /**
     * Generate 7 demo measurements for each measurement type
     * Some values will be inside safe ranges, some slightly outside to demonstrate alerts
     * @param device The device to add measurements to
     */
    private void generateDemoMeasurements(Device device) {
        Random random = new Random();
        
        // Temperature: Safe range 35.0-38.0°C
        // Generate 5 safe values and 2 values outside range
        double[] temperatures = {
            36.0 + random.nextDouble() * 1.5,  // Safe: 36.0-37.5
            36.2 + random.nextDouble() * 1.3,  // Safe: 36.2-37.5
            36.5 + random.nextDouble() * 1.0,  // Safe: 36.5-37.5
            35.8 + random.nextDouble() * 1.8,  // Safe: 35.8-37.6
            36.3 + random.nextDouble() * 1.2,  // Safe: 36.3-37.5
            38.2 + random.nextDouble() * 0.8,  // Unsafe: 38.2-39.0 (high)
            34.2 + random.nextDouble() * 0.6   // Unsafe: 34.2-34.8 (low)
        };
        for (double temp : temperatures) {
            var measurement = new TemperatureMeasurement(temp);
            if (measurement.surpassesThreshold()) {
                device.addDomainEvent(new AlertCreatedEvent(device.getId(), temp, "TEMPERATURE"));
            }
            device.addTemperature(temp);
        }
        
        // Heart Rate: Safe range 50-120 bpm
        // Generate 5 safe values and 2 values outside range
        int[] heartRates = {
            60 + random.nextInt(40),   // Safe: 60-100
            65 + random.nextInt(35),   // Safe: 65-100
            70 + random.nextInt(30),   // Safe: 70-100
            75 + random.nextInt(25),   // Safe: 75-100
            55 + random.nextInt(45),   // Safe: 55-100
            125 + random.nextInt(20),  // Unsafe: 125-145 (high)
            40 + random.nextInt(8)     // Unsafe: 40-48 (low)
        };
        for (int hr : heartRates) {
            var measurement = new HeartRateMeasurement(hr);
            if (measurement.surpassesThreshold()) {
                device.addDomainEvent(new AlertCreatedEvent(device.getId(), hr, "HEART_RATE"));
            }
            device.addHeartRate(hr);
        }
        
        // Oxygen: Safe minimum 90%
        // Generate 5 safe values and 2 values below threshold
        int[] oxygenLevels = {
            95 + random.nextInt(5),    // Safe: 95-100
            93 + random.nextInt(6),    // Safe: 93-99
            94 + random.nextInt(5),    // Safe: 94-99
            96 + random.nextInt(4),    // Safe: 96-100
            92 + random.nextInt(7),    // Safe: 92-99
            85 + random.nextInt(4),    // Unsafe: 85-89
            82 + random.nextInt(5)     // Unsafe: 82-87
        };
        for (int spo2 : oxygenLevels) {
            var measurement = new OxygenMeasurement(spo2);
            if (measurement.surpassesThreshold()) {
                device.addDomainEvent(new AlertCreatedEvent(device.getId(), spo2, "OXYGEN"));
            }
            device.addOxygen(spo2);
        }
        
        // Blood Pressure: Safe range 90-180 for both diastolic and systolic
        // Generate 5 safe pairs and 2 pairs with at least one value outside range
        int[][] bloodPressures = {
            {70 + random.nextInt(15), 110 + random.nextInt(25)},  // Safe: 70-85/110-135
            {72 + random.nextInt(13), 115 + random.nextInt(20)},  // Safe: 72-85/115-135
            {75 + random.nextInt(10), 120 + random.nextInt(15)},  // Safe: 75-85/120-135
            {68 + random.nextInt(17), 112 + random.nextInt(23)},  // Safe: 68-85/112-135
            {74 + random.nextInt(11), 118 + random.nextInt(17)},  // Safe: 74-85/118-135
            {85 + random.nextInt(10), 185 + random.nextInt(15)},  // Unsafe: systolic high
            {55 + random.nextInt(10), 95 + random.nextInt(20)}    // Unsafe: diastolic low
        };
        for (int[] bp : bloodPressures) {
            int diastolic = bp[0];
            int systolic = bp[1];
            var measurement = new BloodPressureMeasurement(diastolic, systolic);
            if (measurement.diastolicSurpassesThreshold()) {
                device.addDomainEvent(new AlertCreatedEvent(device.getId(), diastolic, "BLOOD_PRESSURE"));
            }
            if (measurement.systolicSurpassesThreshold()) {
                device.addDomainEvent(new AlertCreatedEvent(device.getId(), systolic, "BLOOD_PRESSURE"));
            }
            device.addBloodPressure(diastolic, systolic);
        }
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

