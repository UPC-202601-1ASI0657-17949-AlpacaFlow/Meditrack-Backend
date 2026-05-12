package com.alpacaflow.meditrackplatform.organization.interfaces.rest;

import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllSeniorCitizensByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllSeniorCitizensQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetClinicalBackgroundBySeniorCitizenIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetSeniorCitizenByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.ClinicalBackgroundCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.services.ClinicalBackgroundQueryService;
import com.alpacaflow.meditrackplatform.organization.domain.services.SeniorCitizenCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.services.SeniorCitizenQueryService;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.ClinicalBackgroundResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CreateSeniorCitizenResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.SeniorCitizenResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpdateSeniorCitizenResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpsertClinicalBackgroundResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.ClinicalBackgroundAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.CreateSeniorCitizenCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.SeniorCitizenResourceFromEntityAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.UpdateSeniorCitizenCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The {@link SeniorCitizensController} class defines the RESTful API endpoints for the senior citizens.
 */
@RestController
@RequestMapping(value = "/api/v1/senior-citizens", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Senior Citizens", description = "Available Senior Citizen Endpoints")
public class SeniorCitizensController {
    private final SeniorCitizenCommandService seniorCitizenCommandService;
    private final SeniorCitizenQueryService seniorCitizenQueryService;
    private final ClinicalBackgroundQueryService clinicalBackgroundQueryService;
    private final ClinicalBackgroundCommandService clinicalBackgroundCommandService;

    /**
     * Instantiates a new {@link SeniorCitizensController} instance.
     *
     * @param seniorCitizenCommandService The {@link SeniorCitizenCommandService} instance
     * @param seniorCitizenQueryService   The {@link SeniorCitizenQueryService} instance
     */
    public SeniorCitizensController(
            SeniorCitizenCommandService seniorCitizenCommandService,
            SeniorCitizenQueryService seniorCitizenQueryService,
            ClinicalBackgroundQueryService clinicalBackgroundQueryService,
            ClinicalBackgroundCommandService clinicalBackgroundCommandService
    ) {
        this.seniorCitizenCommandService = seniorCitizenCommandService;
        this.seniorCitizenQueryService = seniorCitizenQueryService;
        this.clinicalBackgroundQueryService = clinicalBackgroundQueryService;
        this.clinicalBackgroundCommandService = clinicalBackgroundCommandService;
    }

    /**
     * Create a new senior citizen
     *
     * @param resource The {@link CreateSeniorCitizenResource} instance
     * @return The {@link SeniorCitizenResource} resource for the created senior citizen, or a bad request response if the senior citizen was not created
     */
    @PostMapping
    @Operation(summary = "Create a new senior citizen", description = "Create a new senior citizen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Senior citizen created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Senior citizen not found")})
    public ResponseEntity<SeniorCitizenResource> createSeniorCitizen(@RequestBody CreateSeniorCitizenResource resource) {
        var createSeniorCitizenCommand = CreateSeniorCitizenCommandFromResourceAssembler.toCommandFromResource(resource);
        var seniorCitizenId = seniorCitizenCommandService.handle(createSeniorCitizenCommand);
        if (seniorCitizenId == null) return ResponseEntity.badRequest().build();
        var getSeniorCitizenByIdQuery = new GetSeniorCitizenByIdQuery(seniorCitizenId);
        var seniorCitizen = seniorCitizenQueryService.handle(getSeniorCitizenByIdQuery);
        if (seniorCitizen.isEmpty()) return ResponseEntity.notFound().build();
        var createdSeniorCitizen = seniorCitizen.get();
        var seniorCitizenResource = SeniorCitizenResourceFromEntityAssembler.toResourceFromEntity(createdSeniorCitizen);
        return new ResponseEntity<>(seniorCitizenResource, HttpStatus.CREATED);
    }

    /**
     * Get senior citizen by ID
     *
     * @param seniorCitizenId The senior citizen ID
     * @return The {@link SeniorCitizenResource} resource for the senior citizen, or a not found response if the senior citizen was not found
     */
    @GetMapping("/{seniorCitizenId}")
    @Operation(summary = "Get senior citizen by ID", description = "Get senior citizen by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senior citizen found"),
            @ApiResponse(responseCode = "404", description = "Senior citizen not found")})
    public ResponseEntity<SeniorCitizenResource> getSeniorCitizenById(@PathVariable Long seniorCitizenId) {
        var getSeniorCitizenByIdQuery = new GetSeniorCitizenByIdQuery(seniorCitizenId);
        var seniorCitizen = seniorCitizenQueryService.handle(getSeniorCitizenByIdQuery);
        if (seniorCitizen.isEmpty()) return ResponseEntity.notFound().build();
        var seniorCitizenResource = SeniorCitizenResourceFromEntityAssembler.toResourceFromEntity(seniorCitizen.get());
        return ResponseEntity.ok(seniorCitizenResource);
    }

    /**
     * Get descriptive clinical background for a senior citizen (informational only; not used for IoT alerts).
     */
    @GetMapping("/{seniorCitizenId}/clinical-background")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','CAREGIVER')")
    @Operation(summary = "Get clinical background", description = "Administrative / family-visible clinical notes. Empty defaults if not yet created.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payload returned"),
            @ApiResponse(responseCode = "404", description = "Senior citizen not found")})
    public ResponseEntity<ClinicalBackgroundResource> getClinicalBackground(@PathVariable Long seniorCitizenId) {
        var senior = seniorCitizenQueryService.handle(new GetSeniorCitizenByIdQuery(seniorCitizenId));
        if (senior.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var clinical = clinicalBackgroundQueryService.handle(new GetClinicalBackgroundBySeniorCitizenIdQuery(seniorCitizenId));
        var resource = clinical.map(ClinicalBackgroundAssembler::toResource)
                .orElseGet(() -> ClinicalBackgroundAssembler.emptyShell(seniorCitizenId));
        return ResponseEntity.ok(resource);
    }

    /**
     * Create or update clinical background (Organization actor; set authorRole to ORGANIZATION).
     */
    @PutMapping(value = "/{seniorCitizenId}/clinical-background", consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Upsert clinical background", description = "Does not affect Monitoring thresholds or alerts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saved"),
            @ApiResponse(responseCode = "404", description = "Senior citizen not found")})
    public ResponseEntity<ClinicalBackgroundResource> upsertClinicalBackground(
            @PathVariable Long seniorCitizenId,
            @RequestBody @Valid UpsertClinicalBackgroundResource resource
    ) {
        var command = ClinicalBackgroundAssembler.toCommand(seniorCitizenId, resource);
        clinicalBackgroundCommandService.handle(command);
        var clinical = clinicalBackgroundQueryService.handle(new GetClinicalBackgroundBySeniorCitizenIdQuery(seniorCitizenId));
        return clinical.map(ClinicalBackgroundAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all senior citizens
     *
     * @return A list of {@link SeniorCitizenResource} resources
     */
    @GetMapping
    @Operation(summary = "Get all senior citizens", description = "Get all senior citizens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senior citizens retrieved successfully")})
    public ResponseEntity<List<SeniorCitizenResource>> getAllSeniorCitizens() {
        var getAllSeniorCitizensQuery = new GetAllSeniorCitizensQuery();
        var seniorCitizens = seniorCitizenQueryService.handle(getAllSeniorCitizensQuery);
        var seniorCitizenResources = seniorCitizens.stream()
                .map(SeniorCitizenResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(seniorCitizenResources);
    }

    /**
     * Get all senior citizens by organization ID
     *
     * @param organizationId The organization ID
     * @return A list of {@link SeniorCitizenResource} resources for the organization
     */
    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Get all senior citizens by organization ID", description = "Get all senior citizens by organization ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senior citizens retrieved successfully")})
    public ResponseEntity<List<SeniorCitizenResource>> getAllSeniorCitizensByOrganizationId(@PathVariable Long organizationId) {
        var getAllSeniorCitizensByOrganizationIdQuery = new GetAllSeniorCitizensByOrganizationIdQuery(organizationId);
        var seniorCitizens = seniorCitizenQueryService.handle(getAllSeniorCitizensByOrganizationIdQuery);
        var seniorCitizenResources = seniorCitizens.stream()
                .map(SeniorCitizenResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(seniorCitizenResources);
    }

    /**
     * Update a senior citizen
     *
     * @param seniorCitizenId The senior citizen ID
     * @param resource The {@link UpdateSeniorCitizenResource} instance
     * @return The {@link SeniorCitizenResource} resource for the updated senior citizen, or a not found response if the senior citizen was not found
     */
    @PutMapping("/{seniorCitizenId}")
    @Operation(summary = "Update a senior citizen", description = "Update a senior citizen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senior citizen updated"),
            @ApiResponse(responseCode = "404", description = "Senior citizen not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<SeniorCitizenResource> updateSeniorCitizen(@PathVariable Long seniorCitizenId, @RequestBody UpdateSeniorCitizenResource resource) {
        var updateSeniorCitizenCommand = UpdateSeniorCitizenCommandFromResourceAssembler.toCommandFromResource(resource, seniorCitizenId);
        var seniorCitizen = seniorCitizenCommandService.handle(updateSeniorCitizenCommand);
        if (seniorCitizen.isEmpty()) return ResponseEntity.notFound().build();
        var seniorCitizenResource = SeniorCitizenResourceFromEntityAssembler.toResourceFromEntity(seniorCitizen.get());
        return ResponseEntity.ok(seniorCitizenResource);
    }

    /**
     * Delete a senior citizen
     *
     * @param seniorCitizenId The senior citizen ID
     * @return A no content response if the senior citizen was deleted, or a not found response if the senior citizen was not found
     */
    @DeleteMapping("/{seniorCitizenId}")
    @Operation(summary = "Delete a senior citizen", description = "Delete a senior citizen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senior citizen deleted"),
            @ApiResponse(responseCode = "404", description = "Senior citizen not found")})
    public ResponseEntity<Void> deleteSeniorCitizen(@PathVariable Long seniorCitizenId) {
        var deleteSeniorCitizenCommand = new com.alpacaflow.meditrackplatform.organization.domain.model.commands.DeleteSeniorCitizenCommand(seniorCitizenId);
        try {
            seniorCitizenCommandService.handle(deleteSeniorCitizenCommand);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

