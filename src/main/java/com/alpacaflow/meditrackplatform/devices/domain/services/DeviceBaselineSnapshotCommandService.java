package com.alpacaflow.meditrackplatform.devices.domain.services;

import com.alpacaflow.meditrackplatform.devices.domain.model.commands.UpsertDeviceBaselineSnapshotCommand;

public interface DeviceBaselineSnapshotCommandService {
    void handle(UpsertDeviceBaselineSnapshotCommand command);
}
