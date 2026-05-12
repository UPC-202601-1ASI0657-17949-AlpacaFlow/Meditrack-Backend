package com.alpacaflow.meditrackplatform.devices.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.devices.domain.exceptions.DeviceNotFoundException;
import com.alpacaflow.meditrackplatform.devices.domain.model.commands.UpsertDeviceBaselineSnapshotCommand;
import com.alpacaflow.meditrackplatform.devices.domain.model.entities.DeviceThresholdSnapshot;
import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.PatientVitalBaselineSnapshot;
import com.alpacaflow.meditrackplatform.devices.domain.services.DeviceBaselineSnapshotCommandService;
import com.alpacaflow.meditrackplatform.devices.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.alpacaflow.meditrackplatform.devices.infrastructure.persistence.jpa.repositories.DeviceThresholdSnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceBaselineSnapshotCommandServiceImpl implements DeviceBaselineSnapshotCommandService {

    private final DeviceRepository deviceRepository;
    private final DeviceThresholdSnapshotRepository snapshotRepository;

    public DeviceBaselineSnapshotCommandServiceImpl(
            DeviceRepository deviceRepository,
            DeviceThresholdSnapshotRepository snapshotRepository
    ) {
        this.deviceRepository = deviceRepository;
        this.snapshotRepository = snapshotRepository;
    }

    @Override
    @Transactional
    public void handle(UpsertDeviceBaselineSnapshotCommand command) {
        if (deviceRepository.findById(command.deviceId()).isEmpty()) {
            throw new DeviceNotFoundException(command.deviceId());
        }
        new PatientVitalBaselineSnapshot(
                command.heartRateMin(),
                command.heartRateMax(),
                command.oxygenSaturationMin(),
                command.temperatureMin(),
                command.temperatureMax()
        );

        var row = snapshotRepository.findByDeviceId(command.deviceId());
        if (row.isPresent()) {
            var e = row.get();
            e.setHeartRateMin(command.heartRateMin());
            e.setHeartRateMax(command.heartRateMax());
            e.setOxygenSaturationMin(command.oxygenSaturationMin());
            e.setTemperatureMin(command.temperatureMin());
            e.setTemperatureMax(command.temperatureMax());
            snapshotRepository.save(e);
        } else {
            snapshotRepository.save(new DeviceThresholdSnapshot(
                    command.deviceId(),
                    command.heartRateMin(),
                    command.heartRateMax(),
                    command.oxygenSaturationMin(),
                    command.temperatureMin(),
                    command.temperatureMax()
            ));
        }
    }
}
