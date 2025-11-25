package com.alpacaflow.meditrackplatform.devices.application.acl;

import com.alpacaflow.meditrackplatform.devices.domain.model.commands.CreateDeviceCommand;
import com.alpacaflow.meditrackplatform.devices.domain.services.DeviceCommandService;
import com.alpacaflow.meditrackplatform.devices.domain.services.DeviceQueryService;
import com.alpacaflow.meditrackplatform.devices.interfaces.acl.DevicesContextFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of DevicesContextFacade
 * Provides ACL for external contexts to interact with the Devices bounded context
 */
@Service
public class DevicesContextFacadeImpl implements DevicesContextFacade {
    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    public DevicesContextFacadeImpl(DeviceCommandService deviceCommandService, DeviceQueryService deviceQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long createDevice(String model, Long holderId) {
        var createDeviceCommand = new CreateDeviceCommand(model, holderId);
        // Let exceptions propagate to allow transaction rollback
        // The caller (ExternalDeviceService) should handle the exception
        return deviceCommandService.handle(createDeviceCommand);
    }

    @Override
    public Long fetchDeviceIdByHolderId(Long holderId) {
        // Note: This would require adding a query method to find device by holderId
        // For now, returning 0L as this is primarily for creation
        return 0L;
    }
    
    @Override
    public boolean deviceExists(Long deviceId) {
        if (deviceId == null || deviceId <= 0) {
            return false;
        }
        var query = new com.alpacaflow.meditrackplatform.devices.domain.model.queries.GetDeviceByIdQuery(deviceId);
        var device = deviceQueryService.handle(query);
        return device.isPresent();
    }
}
