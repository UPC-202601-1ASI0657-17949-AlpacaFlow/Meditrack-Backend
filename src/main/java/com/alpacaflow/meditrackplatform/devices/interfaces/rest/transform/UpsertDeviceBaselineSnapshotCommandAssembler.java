package com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.devices.domain.model.commands.UpsertDeviceBaselineSnapshotCommand;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.UpsertDeviceBaselineSnapshotResource;

public final class UpsertDeviceBaselineSnapshotCommandAssembler {

    private UpsertDeviceBaselineSnapshotCommandAssembler() {
    }

    public static UpsertDeviceBaselineSnapshotCommand toCommand(Long deviceId, UpsertDeviceBaselineSnapshotResource resource) {
        return new UpsertDeviceBaselineSnapshotCommand(
                deviceId,
                resource.heartRateMin(),
                resource.heartRateMax(),
                resource.oxygenSaturationMin(),
                resource.temperatureMin(),
                resource.temperatureMax()
        );
    }
}
