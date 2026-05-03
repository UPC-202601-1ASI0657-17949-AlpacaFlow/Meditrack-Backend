package com.alpacaflow.meditrackplatform.devices.application.internal.queryservices;

import com.alpacaflow.meditrackplatform.devices.domain.exceptions.DeviceNotFoundException;
import com.alpacaflow.meditrackplatform.devices.domain.model.aggregates.Device;
import com.alpacaflow.meditrackplatform.devices.domain.model.entities.*;
import com.alpacaflow.meditrackplatform.devices.domain.model.queries.*;
import com.alpacaflow.meditrackplatform.devices.domain.services.DeviceQueryService;
import com.alpacaflow.meditrackplatform.devices.infrastructure.persistence.jpa.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the DeviceQueryService interface.
 * <p>This class is responsible for handling the queries related to the Device aggregate. It requires a DeviceRepository.</p>
 * @see DeviceQueryService
 * @see DeviceRepository
 */
@Service
public class DeviceQueryServiceImpl implements DeviceQueryService {
    private final DeviceRepository deviceRepository;

    public DeviceQueryServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<Device> handle(GetDeviceByIdQuery query) {
        return deviceRepository.findById(query.deviceId());
    }

    @Override
    public List<Device> handle(GetAllDevicesQuery query) {
        return deviceRepository.findAll();
    }

    @Override
    public List<HeartRateMeasurement> handle(GetAllHeartRateMeasurementsByDeviceIdQuery query) {
        var device = deviceRepository.findById(query.deviceId())
                .orElseThrow(() -> new DeviceNotFoundException(query.deviceId()));
        return device.getHeartRateMeasurements();
    }

    @Override
    public List<TemperatureMeasurement> handle(GetAllTemperatureMeasurementsByDeviceIdQuery query) {
        var device = deviceRepository.findById(query.deviceId())
                .orElseThrow(() -> new DeviceNotFoundException(query.deviceId()));
        return device.getTemperatureMeasurements();
    }

    @Override
    public List<OxygenMeasurement> handle(GetAllOxygenMeasurementsByDeviceIdQuery query) {
        var device = deviceRepository.findById(query.deviceId())
                .orElseThrow(() -> new DeviceNotFoundException(query.deviceId()));
        return device.getOxygenMeasurements();
    }
}

