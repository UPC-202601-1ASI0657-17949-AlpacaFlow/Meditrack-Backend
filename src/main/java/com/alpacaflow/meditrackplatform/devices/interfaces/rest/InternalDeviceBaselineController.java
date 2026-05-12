package com.alpacaflow.meditrackplatform.devices.interfaces.rest;

import com.alpacaflow.meditrackplatform.devices.domain.services.DeviceBaselineSnapshotCommandService;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.UpsertDeviceBaselineSnapshotResource;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform.UpsertDeviceBaselineSnapshotCommandAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Internal sync API (Organization → Monitoring). Secured like other APIs until a dedicated service identity exists.
 */
@RestController
@RequestMapping(value = "/api/v1/internal/devices", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Internal Device baseline", description = "Push vital threshold snapshot for hot-path evaluation")
public class InternalDeviceBaselineController {

    private final DeviceBaselineSnapshotCommandService baselineSnapshotCommandService;

    public InternalDeviceBaselineController(DeviceBaselineSnapshotCommandService baselineSnapshotCommandService) {
        this.baselineSnapshotCommandService = baselineSnapshotCommandService;
    }

    @PutMapping(value = "/{deviceId}/baseline", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Upsert device vital baseline snapshot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Snapshot saved successfully (no response body)"),
            @ApiResponse(responseCode = "404", description = "Device not found for the given deviceId")
    })
    public ResponseEntity<Void> upsertBaseline(
            @PathVariable Long deviceId,
            @RequestBody UpsertDeviceBaselineSnapshotResource resource
    ) {
        var command = UpsertDeviceBaselineSnapshotCommandAssembler.toCommand(deviceId, resource);
        baselineSnapshotCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}
