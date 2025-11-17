package com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources;

/**
 * Resource for creating a device
 * @param model The device model
 * @param holderId The holder ID
 */
public record CreateDeviceResource(String model, Long holderId) {
}

