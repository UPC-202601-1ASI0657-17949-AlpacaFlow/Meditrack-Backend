package com.alpacaflow.meditrackplatform.organization.interfaces.rest;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.UnassignSeniorCitizenFromDoctorCommand;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetSeniorCitizenByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.SeniorCitizenCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.services.SeniorCitizenQueryService;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.AssignSeniorCitizenToDoctorResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.SeniorCitizenResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.AssignSeniorCitizenToDoctorCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.SeniorCitizenResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The {@link DoctorAssignmentsController} class defines the RESTful API endpoints for doctor assignments.
 * This controller handles the assignment relationship between SeniorCitizens and Doctors.
 */
@RestController
@RequestMapping(value = "/api/v1/doctor-assignments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Doctor Assignments", description = "Available Doctor Assignment Endpoints")
public class DoctorAssignmentsController {
    private final SeniorCitizenCommandService seniorCitizenCommandService;
    private final SeniorCitizenQueryService seniorCitizenQueryService;

    /**
     * Instantiates a new {@link DoctorAssignmentsController} instance.
     *
     * @param seniorCitizenCommandService The {@link SeniorCitizenCommandService} instance
     * @param seniorCitizenQueryService   The {@link SeniorCitizenQueryService} instance
     */
    public DoctorAssignmentsController(SeniorCitizenCommandService seniorCitizenCommandService, SeniorCitizenQueryService seniorCitizenQueryService) {
        this.seniorCitizenCommandService = seniorCitizenCommandService;
        this.seniorCitizenQueryService = seniorCitizenQueryService;
    }

    /**
     * Assign a senior citizen to a doctor
     *
     * @param resource The {@link AssignSeniorCitizenToDoctorResource} instance
     * @return The {@link SeniorCitizenResource} resource for the assigned senior citizen, or a bad request response if the assignment failed
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Assign a senior citizen to a doctor", description = "Assign a senior citizen to a doctor. A senior citizen can only be assigned to one doctor at a time, and cannot be assigned to both a doctor and a caregiver simultaneously.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senior citizen assigned to doctor successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Senior citizen or doctor not found")})
    public ResponseEntity<SeniorCitizenResource> assignSeniorCitizenToDoctor(@RequestBody AssignSeniorCitizenToDoctorResource resource) {
        System.out.println("📥 [Backend] Received assignment request: seniorCitizenId=" + resource.seniorCitizenId() + ", doctorId=" + resource.doctorId());
        System.out.println("📥 [Backend] Resource object: " + resource);
        
        try {
            var assignCommand = AssignSeniorCitizenToDoctorCommandFromResourceAssembler.toCommandFromResource(resource);
            System.out.println("📥 [Backend] Command created: seniorCitizenId=" + assignCommand.seniorCitizenId() + ", doctorId=" + assignCommand.doctorId());
            
            var seniorCitizen = seniorCitizenCommandService.handle(assignCommand);
            if (seniorCitizen.isEmpty()) {
                System.out.println("❌ [Backend] Assignment failed: seniorCitizen is empty");
                throw new IllegalStateException("Assignment failed: seniorCitizen is empty");
            }
            var seniorCitizenResource = SeniorCitizenResourceFromEntityAssembler.toResourceFromEntity(seniorCitizen.get());
            System.out.println("✅ [Backend] Assignment successful: seniorCitizenId=" + resource.seniorCitizenId() + ", doctorId=" + resource.doctorId());
            return ResponseEntity.ok(seniorCitizenResource);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("❌ [Backend] Assignment error: " + e.getMessage());
            e.printStackTrace();
            // Re-throw to let GlobalExceptionHandler handle it with proper error message
            throw e;
        } catch (Exception e) {
            System.out.println("❌ [Backend] Assignment exception: " + e.getMessage());
            e.printStackTrace();
            // Re-throw to let GlobalExceptionHandler handle it
            throw new RuntimeException("Assignment failed: " + e.getMessage(), e);
        }
    }

    /**
     * Unassign a senior citizen from a doctor
     *
     * @param doctorId         The doctor ID
     * @param seniorCitizenId  The senior citizen ID
     * @return A no content response if the unassignment was successful, or a not found response if it failed
     */
    @DeleteMapping("/doctors/{doctorId}/senior-citizens/{seniorCitizenId}")
    @Operation(summary = "Unassign a senior citizen from a doctor", description = "Unassign a senior citizen from a doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senior citizen unassigned from doctor successfully"),
            @ApiResponse(responseCode = "404", description = "Senior citizen or doctor not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<Void> unassignSeniorCitizenFromDoctor(@PathVariable Long doctorId, @PathVariable Long seniorCitizenId) {
        try {
            var unassignCommand = new UnassignSeniorCitizenFromDoctorCommand(seniorCitizenId, doctorId);
            seniorCitizenCommandService.handle(unassignCommand);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all senior citizens assigned to a doctor
     *
     * @param doctorId The doctor ID
     * @return A list of {@link SeniorCitizenResource} resources assigned to the doctor
     */
    @GetMapping("/doctors/{doctorId}/senior-citizens")
    @Operation(summary = "Get all senior citizens assigned to a doctor", description = "Get all senior citizens assigned to a specific doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senior citizens retrieved successfully")})
    public ResponseEntity<List<SeniorCitizenResource>> getSeniorCitizensByDoctorId(@PathVariable Long doctorId) {
        // Get all senior citizens and filter by assignedDoctorId
        var getAllSeniorCitizensQuery = new com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllSeniorCitizensQuery();
        var allSeniorCitizens = seniorCitizenQueryService.handle(getAllSeniorCitizensQuery);
        var assignedSeniorCitizens = allSeniorCitizens.stream()
                .filter(sc -> sc.getAssignedDoctorId() != null && sc.getAssignedDoctorId().equals(doctorId))
                .map(SeniorCitizenResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(assignedSeniorCitizens);
    }

    /**
     * Get the doctor assigned to a senior citizen
     *
     * @param seniorCitizenId The senior citizen ID
     * @return The {@link SeniorCitizenResource} resource with the assigned doctor information, or a not found response if the senior citizen was not found
     */
    @GetMapping("/senior-citizens/{seniorCitizenId}/doctor")
    @Operation(summary = "Get the doctor assigned to a senior citizen", description = "Get the doctor assigned to a specific senior citizen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senior citizen found"),
            @ApiResponse(responseCode = "404", description = "Senior citizen not found")})
    public ResponseEntity<SeniorCitizenResource> getDoctorBySeniorCitizenId(@PathVariable Long seniorCitizenId) {
        var getSeniorCitizenByIdQuery = new GetSeniorCitizenByIdQuery(seniorCitizenId);
        var seniorCitizen = seniorCitizenQueryService.handle(getSeniorCitizenByIdQuery);
        if (seniorCitizen.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var seniorCitizenResource = SeniorCitizenResourceFromEntityAssembler.toResourceFromEntity(seniorCitizen.get());
        return ResponseEntity.ok(seniorCitizenResource);
    }
}