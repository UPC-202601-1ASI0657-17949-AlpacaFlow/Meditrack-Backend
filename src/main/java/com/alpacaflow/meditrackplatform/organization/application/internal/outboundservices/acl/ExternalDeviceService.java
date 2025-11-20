package com.alpacaflow.meditrackplatform.organization.application.internal.outboundservices.acl;

import com.alpacaflow.meditrackplatform.devices.interfaces.acl.DevicesContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * External Device Service
 * ACL service for Organization context to interact with Devices context
 */
@Service
public class ExternalDeviceService {
    private final DevicesContextFacade devicesContextFacade;

    /**
     * Constructor
     * @param devicesContextFacade Devices Context Facade
     */
    public ExternalDeviceService(DevicesContextFacade devicesContextFacade) {
        this.devicesContextFacade = devicesContextFacade;
    }

    /**
     * Create a device for a senior citizen
     * @param model The device model (default: "MediTrack Wearable v1.0")
     * @param seniorCitizenId The senior citizen ID (holder)
     * @return An Optional containing the device ID, or empty if creation failed
     */
    public Optional<Long> createDeviceForSeniorCitizen(String model, Long seniorCitizenId) {
        var deviceId = devicesContextFacade.createDevice(model, seniorCitizenId);
        return deviceId == 0L ? Optional.empty() : Optional.of(deviceId);
    }

    /**
     * Create a device with default model for a senior citizen
     * @param seniorCitizenId The senior citizen ID (holder)
     * @return An Optional containing the device ID, or empty if creation failed
     */
    public Optional<Long> createDeviceForSeniorCitizen(Long seniorCitizenId) {
        return createDeviceForSeniorCitizen("MediTrack Wearable v1.0", seniorCitizenId);
    }

    /**
     * Fetch device ID by holder (senior citizen) ID
     * @param seniorCitizenId The senior citizen ID
     * @return An Optional containing the device ID, or empty if not found
     */
    public Optional<Long> fetchDeviceIdByHolderId(Long seniorCitizenId) {
        var deviceId = devicesContextFacade.fetchDeviceIdByHolderId(seniorCitizenId);
        return deviceId == 0L ? Optional.empty() : Optional.of(deviceId);
    }
    
    /**
     * Check if a device exists
     * @param deviceId The device ID
     * @return true if the device exists, false otherwise
     */
    public boolean deviceExists(Long deviceId) {
        return devicesContextFacade.deviceExists(deviceId);
    }
}
