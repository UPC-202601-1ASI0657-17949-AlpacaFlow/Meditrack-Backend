package com.alpacaflow.meditrackplatform.devices.interfaces.acl;

/**
 * DevicesContextFacade
 * Anti-Corruption Layer interface for the Devices bounded context
 */
public interface DevicesContextFacade {
    /**
     * Create a new device
     * @param model The device model
     * @param holderId The holder ID (SeniorCitizen ID)
     * @return The device ID, or 0 if creation failed
     */
    Long createDevice(String model, Long holderId);
    
    /**
     * Fetch a device ID by holder ID
     * @param holderId The holder ID
     * @return The device ID, or 0 if not found
     */
    Long fetchDeviceIdByHolderId(Long holderId);
    
    /**
     * Check if a device exists
     * @param deviceId The device ID
     * @return true if the device exists, false otherwise
     */
    boolean deviceExists(Long deviceId);
}
