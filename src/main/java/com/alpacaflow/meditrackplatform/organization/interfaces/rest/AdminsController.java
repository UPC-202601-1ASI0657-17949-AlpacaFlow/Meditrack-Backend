package com.alpacaflow.meditrackplatform.organization.interfaces.rest;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.DeleteAdminCommand;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllAdminsByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllAdminsQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAdminByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.AdminCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.services.AdminQueryService;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.AdminResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CreateAdminResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpdateAdminResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.AdminResourceFromEntityAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.CreateAdminCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.UpdateAdminCommandFromResourceAssembler;
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
 * The {@link AdminsController} class defines the RESTful API endpoints for the admins.
 */
@RestController
@RequestMapping(value = "/api/v1/admins", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Admins", description = "Available Admin Endpoints")
public class AdminsController {
    private final AdminCommandService adminCommandService;
    private final AdminQueryService adminQueryService;

    /**
     * Instantiates a new {@link AdminsController} instance.
     *
     * @param adminCommandService The {@link AdminCommandService} instance
     * @param adminQueryService   The {@link AdminQueryService} instance
     */
    public AdminsController(AdminCommandService adminCommandService, AdminQueryService adminQueryService) {
        this.adminCommandService = adminCommandService;
        this.adminQueryService = adminQueryService;
    }

    /**
     * Create a new admin
     *
     * @param resource The {@link CreateAdminResource} instance
     * @return The {@link AdminResource} resource for the created admin, or a bad request response if the admin was not created
     */
    @PostMapping
    @Operation(summary = "Create a new admin", description = "Create a new admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Admin not found")})
    public ResponseEntity<AdminResource> createAdmin(@RequestBody CreateAdminResource resource) {
        var createAdminCommand = CreateAdminCommandFromResourceAssembler.toCommandFromResource(resource);
        var adminId = adminCommandService.handle(createAdminCommand);
        if (adminId == null) return ResponseEntity.badRequest().build();
        var getAdminByIdQuery = new GetAdminByIdQuery(adminId);
        var admin = adminQueryService.handle(getAdminByIdQuery);
        if (admin.isEmpty()) return ResponseEntity.notFound().build();
        var createdAdmin = admin.get();
        var adminResource = AdminResourceFromEntityAssembler.toResourceFromEntity(createdAdmin);
        return new ResponseEntity<>(adminResource, HttpStatus.CREATED);
    }

    /**
     * Get admin by ID
     *
     * @param adminId The admin ID
     * @return The {@link AdminResource} resource for the admin, or a not found response if the admin was not found
     */
    @GetMapping("/{adminId}")
    @Operation(summary = "Get admin by ID", description = "Get admin by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin found"),
            @ApiResponse(responseCode = "404", description = "Admin not found")})
    public ResponseEntity<AdminResource> getAdminById(@PathVariable Long adminId) {
        var getAdminByIdQuery = new GetAdminByIdQuery(adminId);
        var admin = adminQueryService.handle(getAdminByIdQuery);
        if (admin.isEmpty()) return ResponseEntity.notFound().build();
        var adminResource = AdminResourceFromEntityAssembler.toResourceFromEntity(admin.get());
        return ResponseEntity.ok(adminResource);
    }

    /**
     * Get all admins
     *
     * @return A list of {@link AdminResource} resources
     */
    @GetMapping
    @Operation(summary = "Get all admins", description = "Get all admins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admins retrieved successfully")})
    public ResponseEntity<List<AdminResource>> getAllAdmins() {
        var getAllAdminsQuery = new GetAllAdminsQuery();
        var admins = adminQueryService.handle(getAllAdminsQuery);
        var adminResources = admins.stream()
                .map(AdminResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(adminResources);
    }

    /**
     * Get all admins by organization ID
     *
     * @param organizationId The organization ID
     * @return A list of {@link AdminResource} resources for the organization
     */
    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Get all admins by organization ID", description = "Get all admins by organization ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admins retrieved successfully")})
    public ResponseEntity<List<AdminResource>> getAllAdminsByOrganizationId(@PathVariable Long organizationId) {
        var getAllAdminsByOrganizationIdQuery = new GetAllAdminsByOrganizationIdQuery(organizationId);
        var admins = adminQueryService.handle(getAllAdminsByOrganizationIdQuery);
        var adminResources = admins.stream()
                .map(AdminResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(adminResources);
    }

    /**
     * Get admin by user ID and organization ID
     *
     * @param userId The user ID
     * @param organizationId The organization ID
     * @return The {@link AdminResource} resource for the admin, or a not found response if the admin was not found
     */
    @GetMapping("/user/{userId}/organization/{organizationId}")
    @Operation(summary = "Get admin by user ID and organization ID", description = "Get admin by user ID and organization ID. This ensures the admin belongs to the specified organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin found"),
            @ApiResponse(responseCode = "404", description = "Admin not found")})
    public ResponseEntity<AdminResource> getAdminByUserIdAndOrganizationId(@PathVariable Long userId, @PathVariable Long organizationId) {
        var getAdminByUserIdAndOrganizationIdQuery = new com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAdminByUserIdAndOrganizationIdQuery(userId, organizationId);
        var admin = adminQueryService.handle(getAdminByUserIdAndOrganizationIdQuery);
        if (admin.isEmpty()) return ResponseEntity.notFound().build();
        var adminResource = AdminResourceFromEntityAssembler.toResourceFromEntity(admin.get());
        return ResponseEntity.ok(adminResource);
    }

    /**
     * Update an admin
     *
     * @param adminId The admin ID
     * @param resource The {@link UpdateAdminResource} instance
     * @return The {@link AdminResource} resource for the updated admin, or a not found response if the admin was not found
     */
    @PutMapping("/{adminId}")
    @Operation(summary = "Update an admin", description = "Update an admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin updated"),
            @ApiResponse(responseCode = "404", description = "Admin not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<AdminResource> updateAdmin(@PathVariable Long adminId, @RequestBody UpdateAdminResource resource) {
        var updateAdminCommand = UpdateAdminCommandFromResourceAssembler.toCommandFromResource(resource, adminId);
        var admin = adminCommandService.handle(updateAdminCommand);
        if (admin.isEmpty()) return ResponseEntity.notFound().build();
        var adminResource = AdminResourceFromEntityAssembler.toResourceFromEntity(admin.get());
        return ResponseEntity.ok(adminResource);
    }

    /**
     * Delete an admin
     *
     * @param adminId The admin ID
     * @return A no content response if the admin was deleted, or a not found response if the admin was not found
     */
    @DeleteMapping("/{adminId}")
    @Operation(summary = "Delete an admin", description = "Delete an admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Admin deleted"),
            @ApiResponse(responseCode = "404", description = "Admin not found")})
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long adminId) {
        var deleteAdminCommand = new DeleteAdminCommand(adminId);
        try {
            adminCommandService.handle(deleteAdminCommand);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

