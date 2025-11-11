package com.alpacaflow.meditrackplatform.devices.domain.model.commands;

/**
 * Command to create a device
 * @param model The device model. Cannot be null or blank
 * @param holderId The holder ID. Cannot be null
 */
public record CreateDeviceCommand(String model, Long holderId) {
    public CreateDeviceCommand {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be null or blank");
        }
        if (holderId == null) {
            throw new IllegalArgumentException("Holder ID cannot be null");
        }
    }
}

