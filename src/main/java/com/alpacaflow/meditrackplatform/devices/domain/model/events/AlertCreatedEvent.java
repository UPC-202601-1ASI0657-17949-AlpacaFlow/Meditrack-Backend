package com.alpacaflow.meditrackplatform.devices.domain.model.events;

/**
 * Domain event triggered when an alert is created
 * @param deviceId The device ID that generated the alert
 * @param dataRegistered The measurement value that triggered the alert
 * @param measurementType The type of measurement
 */
public record AlertCreatedEvent(Long deviceId, double dataRegistered, String measurementType) {
}

