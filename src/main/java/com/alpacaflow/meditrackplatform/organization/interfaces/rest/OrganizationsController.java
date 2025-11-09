package com.alpacaflow.meditrackplatform.organization.interfaces.rest;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.DeleteOrganizationCommand;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllOrganizationsQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetOrganizationByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.OrganizationCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.services.OrganizationQueryService;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CreateOrganizationResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.OrganizationResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpdateOrganizationResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.CreateOrganizationCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.OrganizationResourceFromEntityAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.UpdateOrganizationCommandFromResourceAssembler;
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
 * The {@link OrganizationsController} class defines the RESTful API endpoints for the organizations.
 */
@RestController
@RequestMapping(value = "/api/v1/organizations", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Organizations", description = "Available Organization Endpoints")
public class OrganizationsController {
    private final OrganizationCommandService organizationCommandService;
    private final OrganizationQueryService organizationQueryService;

    /**
     * Instantiates a new {@link OrganizationsController} instance.
     *
     * @param organizationCommandService The {@link OrganizationCommandService} instance
     * @param organizationQueryService   The {@link OrganizationQueryService} instance
     */
    public OrganizationsController(OrganizationCommandService organizationCommandService, OrganizationQueryService organizationQueryService) {
        this.organizationCommandService = organizationCommandService;
        this.organizationQueryService = organizationQueryService;
    }

    /**
     * Create a new organization
     *
     * @param resource The {@link CreateOrganizationResource} instance
     * @return The {@link OrganizationResource} resource for the created organization, or a bad request response if the organization was not created
     */
    @PostMapping
    @Operation(summary = "Create a new organization", description = "Create a new organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Organization created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Organization not found")})
    public ResponseEntity<OrganizationResource> createOrganization(@RequestBody CreateOrganizationResource resource) {
        var createOrganizationCommand = CreateOrganizationCommandFromResourceAssembler.toCommandFromResource(resource);
        var organizationId = organizationCommandService.handle(createOrganizationCommand);
        if (organizationId == null) return ResponseEntity.badRequest().build();
        var getOrganizationByIdQuery = new GetOrganizationByIdQuery(organizationId);
        var organization = organizationQueryService.handle(getOrganizationByIdQuery);
        if (organization.isEmpty()) return ResponseEntity.notFound().build();
        var createdOrganization = organization.get();
        var organizationResource = OrganizationResourceFromEntityAssembler.toResourceFromEntity(createdOrganization);
        return new ResponseEntity<>(organizationResource, HttpStatus.CREATED);
    }

    /**
     * Get organization by ID
     *
     * @param organizationId The organization ID
     * @return The {@link OrganizationResource} resource for the organization, or a not found response if the organization was not found
     */
    @GetMapping("/{organizationId}")
    @Operation(summary = "Get organization by ID", description = "Get organization by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization found"),
            @ApiResponse(responseCode = "404", description = "Organization not found")})
    public ResponseEntity<OrganizationResource> getOrganizationById(@PathVariable Long organizationId) {
        var getOrganizationByIdQuery = new GetOrganizationByIdQuery(organizationId);
        var organization = organizationQueryService.handle(getOrganizationByIdQuery);
        if (organization.isEmpty()) return ResponseEntity.notFound().build();
        var organizationResource = OrganizationResourceFromEntityAssembler.toResourceFromEntity(organization.get());
        return ResponseEntity.ok(organizationResource);
    }

    /**
     * Get all organizations
     *
     * @return A list of {@link OrganizationResource} resources
     */
    @GetMapping
    @Operation(summary = "Get all organizations", description = "Get all organizations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organizations retrieved successfully")})
    public ResponseEntity<List<OrganizationResource>> getAllOrganizations() {
        var getAllOrganizationsQuery = new GetAllOrganizationsQuery();
        var organizations = organizationQueryService.handle(getAllOrganizationsQuery);
        var organizationResources = organizations.stream()
                .map(OrganizationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(organizationResources);
    }

    /**
     * Update an organization
     *
     * @param organizationId The organization ID
     * @param resource The {@link UpdateOrganizationResource} instance
     * @return The {@link OrganizationResource} resource for the updated organization, or a not found response if the organization was not found
     */
    @PutMapping("/{organizationId}")
    @Operation(summary = "Update an organization", description = "Update an organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization updated"),
            @ApiResponse(responseCode = "404", description = "Organization not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<OrganizationResource> updateOrganization(@PathVariable Long organizationId, @RequestBody UpdateOrganizationResource resource) {
        var updateOrganizationCommand = UpdateOrganizationCommandFromResourceAssembler.toCommandFromResource(resource, organizationId);
        var organization = organizationCommandService.handle(updateOrganizationCommand);
        if (organization.isEmpty()) return ResponseEntity.notFound().build();
        var organizationResource = OrganizationResourceFromEntityAssembler.toResourceFromEntity(organization.get());
        return ResponseEntity.ok(organizationResource);
    }

    /**
     * Delete an organization
     *
     * @param organizationId The organization ID
     * @return A no content response if the organization was deleted, or a not found response if the organization was not found
     */
    @DeleteMapping("/{organizationId}")
    @Operation(summary = "Delete an organization", description = "Delete an organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Organization deleted"),
            @ApiResponse(responseCode = "404", description = "Organization not found")})
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long organizationId) {
        var deleteOrganizationCommand = new DeleteOrganizationCommand(organizationId);
        try {
            organizationCommandService.handle(deleteOrganizationCommand);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

