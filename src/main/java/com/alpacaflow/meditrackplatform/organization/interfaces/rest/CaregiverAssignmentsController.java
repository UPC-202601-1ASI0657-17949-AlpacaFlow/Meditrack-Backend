package com.alpacaflow.meditrackplatform.organization.interfaces.rest;

import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetSeniorCitizenByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.UnassignSeniorCitizenFromCaregiverCommand;
import com.alpacaflow.meditrackplatform.organization.domain.services.SeniorCitizenCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.services.SeniorCitizenQueryService;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.AssignSeniorCitizenToCaregiverResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.SeniorCitizenResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.AssignSeniorCitizenToCaregiverCommandFromResourceAssembler;
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
 * The {@link CaregiverAssignmentsController} class defines the RESTful API endpoints for caregiver assignments.
 * This controller handles the assignment relationship between SeniorCitizens and Caregivers.
 */
@RestController
@RequestMapping(value = "/api/v1/caregiver-assignments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Caregiver Assignments", description = "Available Caregiver Assignment Endpoints")
public class CaregiverAssignmentsController {
    private final SeniorCitizenCommandService seniorCitizenCommandService;
    private final SeniorCitizenQueryService seniorCitizenQueryService;

    /**
     * Instantiates a new {@link CaregiverAssignmentsController} instance.
     *
     * @param seniorCitizenCommandService The {@link SeniorCitizenCommandService} instance
     * @param seniorCitizenQueryService   The {@link SeniorCitizenQueryService} instance
     */
    public CaregiverAssignmentsController(SeniorCitizenCommandService seniorCitizenCommandService, SeniorCitizenQueryService seniorCitizenQueryService) {
        this.seniorCitizenCommandService = seniorCitizenCommandService;
        this.seniorCitizenQueryService = seniorCitizenQueryService;
    }

    /**
     * Assign a senior citizen to a caregiver
     *
     * @param resource The {@link AssignSeniorCitizenToCaregiverResource} instance
     * @return The {@link SeniorCitizenResource} resource for the assigned senior citizen, or a bad request response if the assignment failed
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Assign a senior citizen to a caregiver", description = "Assign a senior citizen to a caregiver. A senior citizen can only be assigned to one caregiver at a time, and cannot be assigned to both a doctor and a caregiver simultaneously.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senior citizen assigned to caregiver successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Senior citizen or caregiver not found")})
    public ResponseEntity<SeniorCitizenResource> assignSeniorCitizenToCaregiver(@RequestBody AssignSeniorCitizenToCaregiverResource resource) {
        System.out.println("[Backend] Received assignment request: seniorCitizenId=" + resource.seniorCitizenId() + ", caregiverId=" + resource.caregiverId());
        System.out.println("[Backend] Resource object: " + resource);
        
        try {
            var assignCommand = AssignSeniorCitizenToCaregiverCommandFromResourceAssembler.toCommandFromResource(resource);
            System.out.println("[Backend] Command created: seniorCitizenId=" + assignCommand.seniorCitizenId() + ", caregiverId=" + assignCommand.caregiverId());
            
            var seniorCitizen = seniorCitizenCommandService.handle(assignCommand);
            if (seniorCitizen.isEmpty()) {
                System.out.println("[Backend] Assignment failed: seniorCitizen is empty");
                throw new IllegalStateException("Assignment failed: seniorCitizen is empty");
            }
            var seniorCitizenResource = SeniorCitizenResourceFromEntityAssembler.toResourceFromEntity(seniorCitizen.get());
            System.out.println("[Backend] Assignment successful: seniorCitizenId=" + resource.seniorCitizenId() + ", caregiverId=" + resource.caregiverId());
            return ResponseEntity.ok(seniorCitizenResource);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[Backend] Assignment error: " + e.getMessage());
            e.printStackTrace();
            // Re-throw to let GlobalExceptionHandler handle it with proper error message
            throw e;
        } catch (Exception e) {
            System.out.println("[Backend] Assignment exception: " + e.getMessage());
            e.printStackTrace();
            // Re-throw to let GlobalExceptionHandler handle it
            throw new RuntimeException("Assignment failed: " + e.getMessage(), e);
        }
    }

    /**
     * Unassign a senior citizen from a caregiver
     *
     * @param caregiverId         The caregiver ID
     * @param seniorCitizenId  The senior citizen ID
     * @return A no content response if the unassignment was successful, or a not found response if it failed
     */
    @DeleteMapping("/caregivers/{caregiverId}/senior-citizens/{seniorCitizenId}")
    @Operation(summary = "Unassign a senior citizen from a caregiver", description = "Unassign a senior citizen from a caregiver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senior citizen unassigned from caregiver successfully"),
            @ApiResponse(responseCode = "404", description = "Senior citizen or caregiver not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<Void> unassignSeniorCitizenFromCaregiver(@PathVariable Long caregiverId, @PathVariable Long seniorCitizenId) {
        try {
            var unassignCommand = new UnassignSeniorCitizenFromCaregiverCommand(seniorCitizenId, caregiverId);
            seniorCitizenCommandService.handle(unassignCommand);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all senior citizens assigned to a caregiver
     *
     * @param caregiverId The caregiver ID
     * @return A list of {@link SeniorCitizenResource} resources assigned to the caregiver
     */
    @GetMapping("/caregivers/{caregiverId}/senior-citizens")
    @Operation(summary = "Get all senior citizens assigned to a caregiver", description = "Get all senior citizens assigned to a specific caregiver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senior citizens retrieved successfully")})
    public ResponseEntity<List<SeniorCitizenResource>> getSeniorCitizensByCaregiverId(@PathVariable Long caregiverId) {
        // Get all senior citizens and filter by assignedCaregiverId
        var getAllSeniorCitizensQuery = new com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllSeniorCitizensQuery();
        var allSeniorCitizens = seniorCitizenQueryService.handle(getAllSeniorCitizensQuery);
        var assignedSeniorCitizens = allSeniorCitizens.stream()
                .filter(sc -> sc.getAssignedCaregiverId() != null && sc.getAssignedCaregiverId().equals(caregiverId))
                .map(SeniorCitizenResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(assignedSeniorCitizens);
    }

    /**
     * Get the caregiver assigned to a senior citizen
     *
     * @param seniorCitizenId The senior citizen ID
     * @return The {@link SeniorCitizenResource} resource with the assigned caregiver information, or a not found response if the senior citizen was not found
     */
    @GetMapping("/senior-citizens/{seniorCitizenId}/caregiver")
    @Operation(summary = "Get the caregiver assigned to a senior citizen", description = "Get the caregiver assigned to a specific senior citizen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senior citizen found"),
            @ApiResponse(responseCode = "404", description = "Senior citizen not found")})
    public ResponseEntity<SeniorCitizenResource> getCaregiverBySeniorCitizenId(@PathVariable Long seniorCitizenId) {
        var getSeniorCitizenByIdQuery = new GetSeniorCitizenByIdQuery(seniorCitizenId);
        var seniorCitizen = seniorCitizenQueryService.handle(getSeniorCitizenByIdQuery);
        if (seniorCitizen.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var seniorCitizenResource = SeniorCitizenResourceFromEntityAssembler.toResourceFromEntity(seniorCitizen.get());
        return ResponseEntity.ok(seniorCitizenResource);
    }
}

