package com.alpacaflow.meditrackplatform.devices.interfaces.rest;

import com.alpacaflow.meditrackplatform.devices.domain.model.queries.GetAlertByIdQuery;
import com.alpacaflow.meditrackplatform.devices.domain.model.queries.GetAllAlertsQuery;
import com.alpacaflow.meditrackplatform.devices.domain.services.AlertQueryService;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.resources.AlertResource;
import com.alpacaflow.meditrackplatform.devices.interfaces.rest.transform.AlertResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * AlertsController
 * <p>
 *     All alert-related endpoints.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/alerts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Alerts", description = "Available Alert Endpoints")
public class AlertsController {
    private final AlertQueryService alertQueryService;

    public AlertsController(AlertQueryService alertQueryService) {
        this.alertQueryService = alertQueryService;
    }

    /**
     * Get alert by id
     * @param alertId The alert id
     * @return The {@link AlertResource} resource for the alert
     */
    @GetMapping("/{alertId}")
    @Operation(summary = "Get alert by id", description = "Get alert by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert found"),
            @ApiResponse(responseCode = "404", description = "Alert not found")})
    public ResponseEntity<AlertResource> getAlertById(@PathVariable Long alertId) {
        var getAlertByIdQuery = new GetAlertByIdQuery(alertId);
        var alert = alertQueryService.handle(getAlertByIdQuery);
        if (alert.isEmpty()) return ResponseEntity.notFound().build();
        var alertEntity = alert.get();
        var alertResource = AlertResourceFromEntityAssembler.toResourceFromEntity(alertEntity);
        return ResponseEntity.ok(alertResource);
    }

    /**
     * Get all alerts
     * @return The list of {@link AlertResource} resources for all alerts
     */
    @GetMapping
    @Operation(summary = "Get all alerts", description = "Get all alerts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerts found")})
    public ResponseEntity<List<AlertResource>> getAllAlerts() {
        var getAllAlertsQuery = new GetAllAlertsQuery();
        var alerts = alertQueryService.handle(getAllAlertsQuery);
        var alertResources = alerts.stream()
                .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(alertResources);
    }
}

