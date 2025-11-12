package com.alpacaflow.meditrackplatform.organization.interfaces.rest;

import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllCaregiversByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllCaregiversQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetCaregiverByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.CaregiverCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.services.CaregiverQueryService;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CaregiverResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CreateCaregiverResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpdateCaregiverResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.CaregiverResourceFromEntityAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.CreateCaregiverCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.UpdateCaregiverCommandFromResourceAssembler;
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
 * The {@link CaregiversController} class defines the RESTful API endpoints for the caregivers.
 */
@RestController
@RequestMapping(value = "/api/v1/caregivers", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Caregivers", description = "Available Caregiver Endpoints")
public class CaregiversController {
    private final CaregiverCommandService caregiverCommandService;
    private final CaregiverQueryService caregiverQueryService;

    /**
     * Instantiates a new {@link CaregiversController} instance.
     *
     * @param caregiverCommandService The {@link CaregiverCommandService} instance
     * @param caregiverQueryService   The {@link CaregiverQueryService} instance
     */
    public CaregiversController(CaregiverCommandService caregiverCommandService, CaregiverQueryService caregiverQueryService) {
        this.caregiverCommandService = caregiverCommandService;
        this.caregiverQueryService = caregiverQueryService;
    }

    /**
     * Create a new caregiver
     *
     * @param resource The {@link CreateCaregiverResource} instance
     * @return The {@link CaregiverResource} resource for the created caregiver, or a bad request response if the caregiver was not created
     */
    @PostMapping
    @Operation(summary = "Create a new caregiver", description = "Create a new caregiver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Caregiver created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Caregiver not found")})
    public ResponseEntity<CaregiverResource> createCaregiver(@RequestBody CreateCaregiverResource resource) {
        var createCaregiverCommand = CreateCaregiverCommandFromResourceAssembler.toCommandFromResource(resource);
        var caregiverId = caregiverCommandService.handle(createCaregiverCommand);
        if (caregiverId == null) return ResponseEntity.badRequest().build();
        var getCaregiverByIdQuery = new GetCaregiverByIdQuery(caregiverId);
        var caregiver = caregiverQueryService.handle(getCaregiverByIdQuery);
        if (caregiver.isEmpty()) return ResponseEntity.notFound().build();
        var createdCaregiver = caregiver.get();
        var caregiverResource = CaregiverResourceFromEntityAssembler.toResourceFromEntity(createdCaregiver);
        return new ResponseEntity<>(caregiverResource, HttpStatus.CREATED);
    }

    /**
     * Get caregiver by ID
     *
     * @param caregiverId The caregiver ID
     * @return The {@link CaregiverResource} resource for the caregiver, or a not found response if the caregiver was not found
     */
    @GetMapping("/{caregiverId}")
    @Operation(summary = "Get caregiver by ID", description = "Get caregiver by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caregiver found"),
            @ApiResponse(responseCode = "404", description = "Caregiver not found")})
    public ResponseEntity<CaregiverResource> getCaregiverById(@PathVariable Long caregiverId) {
        var getCaregiverByIdQuery = new GetCaregiverByIdQuery(caregiverId);
        var caregiver = caregiverQueryService.handle(getCaregiverByIdQuery);
        if (caregiver.isEmpty()) return ResponseEntity.notFound().build();
        var caregiverResource = CaregiverResourceFromEntityAssembler.toResourceFromEntity(caregiver.get());
        return ResponseEntity.ok(caregiverResource);
    }

    /**
     * Get all caregivers
     *
     * @return A list of {@link CaregiverResource} resources
     */
    @GetMapping
    @Operation(summary = "Get all caregivers", description = "Get all caregivers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caregivers retrieved successfully")})
    public ResponseEntity<List<CaregiverResource>> getAllCaregivers() {
        var getAllCaregiversQuery = new GetAllCaregiversQuery();
        var caregivers = caregiverQueryService.handle(getAllCaregiversQuery);
        var caregiverResources = caregivers.stream()
                .map(CaregiverResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(caregiverResources);
    }

    /**
     * Get all caregivers by organization ID
     *
     * @param organizationId The organization ID
     * @return A list of {@link CaregiverResource} resources for the organization
     */
    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Get all caregivers by organization ID", description = "Get all caregivers by organization ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caregivers retrieved successfully")})
    public ResponseEntity<List<CaregiverResource>> getAllCaregiversByOrganizationId(@PathVariable Long organizationId) {
        var getAllCaregiversByOrganizationIdQuery = new GetAllCaregiversByOrganizationIdQuery(organizationId);
        var caregivers = caregiverQueryService.handle(getAllCaregiversByOrganizationIdQuery);
        var caregiverResources = caregivers.stream()
                .map(CaregiverResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(caregiverResources);
    }

    /**
     * Get caregiver by user ID
     *
     * @param userId The user ID
     * @return The {@link CaregiverResource} resource for the caregiver, or a not found response if the caregiver was not found
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get caregiver by user ID", description = "Get caregiver by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caregiver found"),
            @ApiResponse(responseCode = "404", description = "Caregiver not found")})
    public ResponseEntity<CaregiverResource> getCaregiverByUserId(@PathVariable Long userId) {
        var getCaregiverByUserIdQuery = new com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetCaregiverByUserIdQuery(userId);
        var caregiver = caregiverQueryService.handle(getCaregiverByUserIdQuery);
        if (caregiver.isEmpty()) return ResponseEntity.notFound().build();
        var caregiverResource = CaregiverResourceFromEntityAssembler.toResourceFromEntity(caregiver.get());
        return ResponseEntity.ok(caregiverResource);
    }

    /**
     * Get caregiver by user ID and organization ID
     *
     * @param userId The user ID
     * @param organizationId The organization ID
     * @return The {@link CaregiverResource} resource for the caregiver, or a not found response if the caregiver was not found
     */
    @GetMapping("/user/{userId}/organization/{organizationId}")
    @Operation(summary = "Get caregiver by user ID and organization ID", description = "Get caregiver by user ID and organization ID. This ensures the caregiver belongs to the specified organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caregiver found"),
            @ApiResponse(responseCode = "404", description = "Caregiver not found")})
    public ResponseEntity<CaregiverResource> getCaregiverByUserIdAndOrganizationId(@PathVariable Long userId, @PathVariable Long organizationId) {
        var getCaregiverByUserIdAndOrganizationIdQuery = new com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetCaregiverByUserIdAndOrganizationIdQuery(userId, organizationId);
        var caregiver = caregiverQueryService.handle(getCaregiverByUserIdAndOrganizationIdQuery);
        if (caregiver.isEmpty()) return ResponseEntity.notFound().build();
        var caregiverResource = CaregiverResourceFromEntityAssembler.toResourceFromEntity(caregiver.get());
        return ResponseEntity.ok(caregiverResource);
    }

    /**
     * Update a caregiver
     *
     * @param caregiverId The caregiver ID
     * @param resource The {@link UpdateCaregiverResource} instance
     * @return The {@link CaregiverResource} resource for the updated caregiver, or a not found response if the caregiver was not found
     */
    @PutMapping("/{caregiverId}")
    @Operation(summary = "Update a caregiver", description = "Update a caregiver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caregiver updated"),
            @ApiResponse(responseCode = "404", description = "Caregiver not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<CaregiverResource> updateCaregiver(@PathVariable Long caregiverId, @RequestBody UpdateCaregiverResource resource) {
        var updateCaregiverCommand = UpdateCaregiverCommandFromResourceAssembler.toCommandFromResource(resource, caregiverId);
        var caregiver = caregiverCommandService.handle(updateCaregiverCommand);
        if (caregiver.isEmpty()) return ResponseEntity.notFound().build();
        var caregiverResource = CaregiverResourceFromEntityAssembler.toResourceFromEntity(caregiver.get());
        return ResponseEntity.ok(caregiverResource);
    }

    /**
     * Delete a caregiver
     *
     * @param caregiverId The caregiver ID
     * @return A no content response if the caregiver was deleted, or a not found response if the caregiver was not found
     */
    @DeleteMapping("/{caregiverId}")
    @Operation(summary = "Delete a caregiver", description = "Delete a caregiver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Caregiver deleted"),
            @ApiResponse(responseCode = "404", description = "Caregiver not found")})
    public ResponseEntity<Void> deleteCaregiver(@PathVariable Long caregiverId) {
        var deleteCaregiverCommand = new com.alpacaflow.meditrackplatform.organization.domain.model.commands.DeleteCaregiverCommand(caregiverId);
        try {
            caregiverCommandService.handle(deleteCaregiverCommand);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

