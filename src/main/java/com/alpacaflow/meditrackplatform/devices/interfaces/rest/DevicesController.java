package com.alpacaflow.meditrackplatform.devices.interfaces.rest;

import com.alpacaflow.meditrackplatform.devices.domain.model.queries.*;
import com.alpacaflow.meditrackplatform.devices.domain.services.DeviceCommandService;
import com.alpacaflow.meditrackplatform.devices.domain.services.DeviceQueryService;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.*;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * DevicesController
 * <p>
 *     All device-related endpoints.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/devices", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Devices", description = "Available Device Endpoints")
public class DevicesController {
    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    public DevicesController(DeviceCommandService deviceCommandService, DeviceQueryService deviceQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }

    /**
     * Create a new device
     * @param resource The {@link CreateDeviceResource} instance
     * @return The {@link DeviceResource} resource for the created device
     */
    @PostMapping
    @Operation(summary = "Create a new device", description = "Create a new device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    public ResponseEntity<DeviceResource> createDevice(@RequestBody CreateDeviceResource resource) {
        var createDeviceCommand = CreateDeviceCommandFromResourceAssembler.toCommandFromResource(resource);
        var deviceId = deviceCommandService.handle(createDeviceCommand);
        if (deviceId == null || deviceId == 0L) return ResponseEntity.badRequest().build();
        var getDeviceByIdQuery = new GetDeviceByIdQuery(deviceId);
        var device = deviceQueryService.handle(getDeviceByIdQuery);
        if (device.isEmpty()) return ResponseEntity.notFound().build();
        var deviceEntity = device.get();
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(deviceEntity);
        return new ResponseEntity<>(deviceResource, HttpStatus.CREATED);
    }

    /**
     * Get device by id
     * @param deviceId The device id
     * @return The {@link DeviceResource} resource for the device
     */
    @GetMapping("/{deviceId}")
    @Operation(summary = "Get device by id", description = "Get device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device found"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    public ResponseEntity<DeviceResource> getDeviceById(@PathVariable Long deviceId) {
        var getDeviceByIdQuery = new GetDeviceByIdQuery(deviceId);
        var device = deviceQueryService.handle(getDeviceByIdQuery);
        if (device.isEmpty()) return ResponseEntity.notFound().build();
        var deviceEntity = device.get();
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(deviceEntity);
        return ResponseEntity.ok(deviceResource);
    }

    /**
     * Get all devices
     * @return The list of {@link DeviceResource} resources for all devices
     */
    @GetMapping
    @Operation(summary = "Get all devices", description = "Get all devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices found")})
    public ResponseEntity<List<DeviceResource>> getAllDevices() {
        var getAllDevicesQuery = new GetAllDevicesQuery();
        var devices = deviceQueryService.handle(getAllDevicesQuery);
        var deviceResources = devices.stream()
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(deviceResources);
    }

    /**
     * Add a blood pressure measurement to a device
     * @param deviceId The device id
     * @param resource The {@link AddBloodPressureMeasurementToDeviceResource} instance
     * @return The {@link DeviceResource} resource for the updated device
     */
    @PostMapping("/{deviceId}/measurements/blood-pressure")
    @Operation(summary = "Add a blood pressure measurement to a device",
               description = "Add a blood pressure measurement to a device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Measurement added"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    public ResponseEntity<DeviceResource> addBloodPressureMeasurementToDevice(
            @PathVariable Long deviceId,
            @RequestBody AddBloodPressureMeasurementToDeviceResource resource) {
        var command = AddBloodPressureMeasurementToDeviceCommandFromResourceAssembler
                .toCommandFromResource(resource, deviceId);
        var device = deviceCommandService.handle(command);
        if (device.isEmpty()) return ResponseEntity.badRequest().build();
        var deviceEntity = device.get();
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(deviceEntity);
        return new ResponseEntity<>(deviceResource, HttpStatus.CREATED);
    }

    /**
     * Add a heart rate measurement to a device
     * @param deviceId The device id
     * @param resource The {@link AddHeartRateMeasurementToDeviceResource} instance
     * @return The {@link DeviceResource} resource for the updated device
     */
    @PostMapping("/{deviceId}/measurements/heart-rate")
    @Operation(summary = "Add a heart rate measurement to a device",
               description = "Add a heart rate measurement to a device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Measurement added"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    public ResponseEntity<DeviceResource> addHeartRateMeasurementToDevice(
            @PathVariable Long deviceId,
            @RequestBody AddHeartRateMeasurementToDeviceResource resource) {
        var command = AddHeartRateMeasurementToDeviceCommandFromResourceAssembler
                .toCommandFromResource(resource, deviceId);
        var device = deviceCommandService.handle(command);
        if (device.isEmpty()) return ResponseEntity.badRequest().build();
        var deviceEntity = device.get();
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(deviceEntity);
        return new ResponseEntity<>(deviceResource, HttpStatus.CREATED);
    }

    /**
     * Add a temperature measurement to a device
     * @param deviceId The device id
     * @param resource The {@link AddTemperatureMeasurementToDeviceResource} instance
     * @return The {@link DeviceResource} resource for the updated device
     */
    @PostMapping("/{deviceId}/measurements/temperature")
    @Operation(summary = "Add a temperature measurement to a device",
               description = "Add a temperature measurement to a device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Measurement added"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    public ResponseEntity<DeviceResource> addTemperatureMeasurementToDevice(
            @PathVariable Long deviceId,
            @RequestBody AddTemperatureMeasurementToDeviceResource resource) {
        var command = AddTemperatureMeasurementToDeviceCommandFromResourceAssembler
                .toCommandFromResource(resource, deviceId);
        var device = deviceCommandService.handle(command);
        if (device.isEmpty()) return ResponseEntity.badRequest().build();
        var deviceEntity = device.get();
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(deviceEntity);
        return new ResponseEntity<>(deviceResource, HttpStatus.CREATED);
    }

    /**
     * Add an oxygen saturation measurement to a device
     * @param deviceId The device id
     * @param resource The {@link AddOxygenMeasurementToDeviceResource} instance
     * @return The {@link DeviceResource} resource for the updated device
     */
    @PostMapping("/{deviceId}/measurements/oxygen")
    @Operation(summary = "Add an oxygen saturation measurement to a device",
               description = "Add an oxygen saturation measurement to a device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Measurement added"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    public ResponseEntity<DeviceResource> addOxygenMeasurementToDevice(
            @PathVariable Long deviceId,
            @RequestBody AddOxygenMeasurementToDeviceResource resource) {
        var command = AddOxygenMeasurementToDeviceCommandFromResourceAssembler
                .toCommandFromResource(resource, deviceId);
        var device = deviceCommandService.handle(command);
        if (device.isEmpty()) return ResponseEntity.badRequest().build();
        var deviceEntity = device.get();
        var deviceResource = DeviceResourceFromEntityAssembler.toResourceFromEntity(deviceEntity);
        return new ResponseEntity<>(deviceResource, HttpStatus.CREATED);
    }

    /**
     * Get all blood pressure measurements by device id
     * @param deviceId The device id
     * @return The list of {@link BloodPressureMeasurementResource} resources
     */
    @GetMapping("/{deviceId}/measurements/blood-pressure")
    @Operation(summary = "Get all blood pressure measurements by device id",
               description = "Get all blood pressure measurements by device id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurements found"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    public ResponseEntity<List<BloodPressureMeasurementResource>> getAllBloodPressureMeasurementsByDeviceId(
            @PathVariable Long deviceId) {
        var query = new GetAllBloodPressureMeasurementsByDeviceIdQuery(deviceId);
        var measurements = deviceQueryService.handle(query);
        var measurementResources = measurements.stream()
                .map(BloodPressureMeasurementResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(measurementResources);
    }

    /**
     * Get all heart rate measurements by device id
     * @param deviceId The device id
     * @return The list of {@link HeartRateMeasurementResource} resources
     */
    @GetMapping("/{deviceId}/measurements/heart-rate")
    @Operation(summary = "Get all heart rate measurements by device id",
               description = "Get all heart rate measurements by device id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurements found"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    public ResponseEntity<List<HeartRateMeasurementResource>> getAllHeartRateMeasurementsByDeviceId(
            @PathVariable Long deviceId) {
        var query = new GetAllHeartRateMeasurementsByDeviceIdQuery(deviceId);
        var measurements = deviceQueryService.handle(query);
        var measurementResources = measurements.stream()
                .map(HeartRateMeasurementResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(measurementResources);
    }

    /**
     * Get all temperature measurements by device id
     * @param deviceId The device id
     * @return The list of {@link TemperatureMeasurementResource} resources
     */
    @GetMapping("/{deviceId}/measurements/temperature")
    @Operation(summary = "Get all temperature measurements by device id",
               description = "Get all temperature measurements by device id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurements found"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    public ResponseEntity<List<TemperatureMeasurementResource>> getAllTemperatureMeasurementsByDeviceId(
            @PathVariable Long deviceId) {
        var query = new GetAllTemperatureMeasurementsByDeviceIdQuery(deviceId);
        var measurements = deviceQueryService.handle(query);
        var measurementResources = measurements.stream()
                .map(TemperatureMeasurementResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(measurementResources);
    }

    /**
     * Get all oxygen saturation measurements by device id
     * @param deviceId The device id
     * @return The list of {@link OxygenMeasurementResource} resources
     */
    @GetMapping("/{deviceId}/measurements/oxygen")
    @Operation(summary = "Get all oxygen saturation measurements by device id",
               description = "Get all oxygen saturation measurements by device id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurements found"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    public ResponseEntity<List<OxygenMeasurementResource>> getAllOxygenMeasurementsByDeviceId(
            @PathVariable Long deviceId) {
        var query = new GetAllOxygenMeasurementsByDeviceIdQuery(deviceId);
        var measurements = deviceQueryService.handle(query);
        var measurementResources = measurements.stream()
                .map(OxygenMeasurementResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(measurementResources);
    }
}

