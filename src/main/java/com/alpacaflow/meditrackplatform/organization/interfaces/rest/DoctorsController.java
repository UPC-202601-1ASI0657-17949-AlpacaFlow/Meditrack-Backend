package com.alpacaflow.meditrackplatform.organization.interfaces.rest;

import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllDoctorsByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllDoctorsQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetDoctorByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.DoctorCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.services.DoctorQueryService;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CreateDoctorResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.DoctorResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpdateDoctorResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.CreateDoctorCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.DoctorResourceFromEntityAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.UpdateDoctorCommandFromResourceAssembler;
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
 * The {@link DoctorsController} class defines the RESTful API endpoints for the doctors.
 */
@RestController
@RequestMapping(value = "/api/v1/doctors", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Doctors", description = "Available Doctor Endpoints")
public class DoctorsController {
    private final DoctorCommandService doctorCommandService;
    private final DoctorQueryService doctorQueryService;

    /**
     * Instantiates a new {@link DoctorsController} instance.
     *
     * @param doctorCommandService The {@link DoctorCommandService} instance
     * @param doctorQueryService   The {@link DoctorQueryService} instance
     */
    public DoctorsController(DoctorCommandService doctorCommandService, DoctorQueryService doctorQueryService) {
        this.doctorCommandService = doctorCommandService;
        this.doctorQueryService = doctorQueryService;
    }

    /**
     * Create a new doctor
     *
     * @param resource The {@link CreateDoctorResource} instance
     * @return The {@link DoctorResource} resource for the created doctor, or a bad request response if the doctor was not created
     */
    @PostMapping
    @Operation(summary = "Create a new doctor", description = "Create a new doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")})
    public ResponseEntity<DoctorResource> createDoctor(@RequestBody CreateDoctorResource resource) {
        var createDoctorCommand = CreateDoctorCommandFromResourceAssembler.toCommandFromResource(resource);
        var doctorId = doctorCommandService.handle(createDoctorCommand);
        if (doctorId == null) return ResponseEntity.badRequest().build();
        var getDoctorByIdQuery = new GetDoctorByIdQuery(doctorId);
        var doctor = doctorQueryService.handle(getDoctorByIdQuery);
        if (doctor.isEmpty()) return ResponseEntity.notFound().build();
        var createdDoctor = doctor.get();
        var doctorResource = DoctorResourceFromEntityAssembler.toResourceFromEntity(createdDoctor);
        return new ResponseEntity<>(doctorResource, HttpStatus.CREATED);
    }

    /**
     * Get doctor by ID
     *
     * @param doctorId The doctor ID
     * @return The {@link DoctorResource} resource for the doctor, or a not found response if the doctor was not found
     */
    @GetMapping("/{doctorId}")
    @Operation(summary = "Get doctor by ID", description = "Get doctor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")})
    public ResponseEntity<DoctorResource> getDoctorById(@PathVariable Long doctorId) {
        var getDoctorByIdQuery = new GetDoctorByIdQuery(doctorId);
        var doctor = doctorQueryService.handle(getDoctorByIdQuery);
        if (doctor.isEmpty()) return ResponseEntity.notFound().build();
        var doctorResource = DoctorResourceFromEntityAssembler.toResourceFromEntity(doctor.get());
        return ResponseEntity.ok(doctorResource);
    }

    /**
     * Get all doctors
     *
     * @return A list of {@link DoctorResource} resources
     */
    @GetMapping
    @Operation(summary = "Get all doctors", description = "Get all doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors retrieved successfully")})
    public ResponseEntity<List<DoctorResource>> getAllDoctors() {
        var getAllDoctorsQuery = new GetAllDoctorsQuery();
        var doctors = doctorQueryService.handle(getAllDoctorsQuery);
        var doctorResources = doctors.stream()
                .map(DoctorResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(doctorResources);
    }

    /**
     * Get all doctors by organization ID
     *
     * @param organizationId The organization ID
     * @return A list of {@link DoctorResource} resources for the organization
     */
    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Get all doctors by organization ID", description = "Get all doctors by organization ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors retrieved successfully")})
    public ResponseEntity<List<DoctorResource>> getAllDoctorsByOrganizationId(@PathVariable Long organizationId) {
        var getAllDoctorsByOrganizationIdQuery = new GetAllDoctorsByOrganizationIdQuery(organizationId);
        var doctors = doctorQueryService.handle(getAllDoctorsByOrganizationIdQuery);
        var doctorResources = doctors.stream()
                .map(DoctorResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(doctorResources);
    }

    /**
     * Get doctor by user ID
     *
     * @param userId The user ID
     * @return The {@link DoctorResource} resource for the doctor, or a not found response if the doctor was not found
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get doctor by user ID", description = "Get doctor by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")})
    public ResponseEntity<DoctorResource> getDoctorByUserId(@PathVariable Long userId) {
        var getDoctorByUserIdQuery = new com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetDoctorByUserIdQuery(userId);
        var doctor = doctorQueryService.handle(getDoctorByUserIdQuery);
        if (doctor.isEmpty()) return ResponseEntity.notFound().build();
        var doctorResource = DoctorResourceFromEntityAssembler.toResourceFromEntity(doctor.get());
        return ResponseEntity.ok(doctorResource);
    }

    /**
     * Get doctor by user ID and organization ID
     *
     * @param userId The user ID
     * @param organizationId The organization ID
     * @return The {@link DoctorResource} resource for the doctor, or a not found response if the doctor was not found
     */
    @GetMapping("/user/{userId}/organization/{organizationId}")
    @Operation(summary = "Get doctor by user ID and organization ID", description = "Get doctor by user ID and organization ID. This ensures the doctor belongs to the specified organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")})
    public ResponseEntity<DoctorResource> getDoctorByUserIdAndOrganizationId(@PathVariable Long userId, @PathVariable Long organizationId) {
        var getDoctorByUserIdAndOrganizationIdQuery = new com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetDoctorByUserIdAndOrganizationIdQuery(userId, organizationId);
        var doctor = doctorQueryService.handle(getDoctorByUserIdAndOrganizationIdQuery);
        if (doctor.isEmpty()) return ResponseEntity.notFound().build();
        var doctorResource = DoctorResourceFromEntityAssembler.toResourceFromEntity(doctor.get());
        return ResponseEntity.ok(doctorResource);
    }

    /**
     * Update a doctor
     *
     * @param doctorId The doctor ID
     * @param resource The {@link UpdateDoctorResource} instance
     * @return The {@link DoctorResource} resource for the updated doctor, or a not found response if the doctor was not found
     */
    @PutMapping("/{doctorId}")
    @Operation(summary = "Update a doctor", description = "Update a doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor updated"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<DoctorResource> updateDoctor(@PathVariable Long doctorId, @RequestBody UpdateDoctorResource resource) {
        var updateDoctorCommand = UpdateDoctorCommandFromResourceAssembler.toCommandFromResource(resource, doctorId);
        var doctor = doctorCommandService.handle(updateDoctorCommand);
        if (doctor.isEmpty()) return ResponseEntity.notFound().build();
        var doctorResource = DoctorResourceFromEntityAssembler.toResourceFromEntity(doctor.get());
        return ResponseEntity.ok(doctorResource);
    }

    /**
     * Delete a doctor
     *
     * @param doctorId The doctor ID
     * @return A no content response if the doctor was deleted, or a not found response if the doctor was not found
     */
    @DeleteMapping("/{doctorId}")
    @Operation(summary = "Delete a doctor", description = "Delete a doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor deleted"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")})
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long doctorId) {
        var deleteDoctorCommand = new com.alpacaflow.meditrackplatform.organization.domain.model.commands.DeleteDoctorCommand(doctorId);
        try {
            doctorCommandService.handle(deleteDoctorCommand);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

