package com.alpacaflow.meditrackplatform.clinical.interfaces.rest;

import com.alpacaflow.meditrackplatform.clinical.domain.model.queries.GetAllPatientThresholdQuery;
import com.alpacaflow.meditrackplatform.clinical.domain.model.queries.GetPatientThresholdBySeniorCitizenIdQuery;
import com.alpacaflow.meditrackplatform.clinical.domain.services.PatientThresholdCommandService;
import com.alpacaflow.meditrackplatform.clinical.domain.services.PatientThresholdQueryService;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.CreatePatientThresholdResource;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.PatientThresholdResource;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.UpdatePatientThresholdResource;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform.CreatePatientThresholdCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform.PatientThresholdResourceFromEntityAssembler;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform.UpdatePatientThresholdCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/patient-thresholds", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Patient Thresholds", description = "Endpoints de gestión de umbrales clínicos por paciente")
public class PatientThresholdsController {

    private final PatientThresholdCommandService thresholdCommandService;
    private final PatientThresholdQueryService thresholdQueryService;
    private final PatientThresholdQueryService patientThresholdQueryService;

    public PatientThresholdsController(PatientThresholdCommandService thresholdCommandService, PatientThresholdQueryService thresholdQueryService, PatientThresholdQueryService patientThresholdQueryService) {
        this.thresholdCommandService = thresholdCommandService;
        this.thresholdQueryService = thresholdQueryService;
        this.patientThresholdQueryService = patientThresholdQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a threshold for a patient", description = "Create a threshold for a patient")
    public ResponseEntity<PatientThresholdResource> createPatientThreshold(@RequestBody CreatePatientThresholdResource resource) {
        var command = CreatePatientThresholdCommandFromResourceAssembler.toCommandFromResource(resource);
        var patientThreshold = thresholdCommandService.handle(command);

        return patientThreshold
                .map(threshold -> new ResponseEntity<>(PatientThresholdResourceFromEntityAssembler.toResourceFromEntity(threshold), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/senior-citizen/{seniorCitizenId}")
    @Operation(summary = "Get a patient's threshold", description = "Get a patient's threshold")
    public ResponseEntity<PatientThresholdResource> getThresholdBySeniorCitizenId(@PathVariable Long seniorCitizenId) {
        var query = new GetPatientThresholdBySeniorCitizenIdQuery(seniorCitizenId);
        var patientThreshold = thresholdQueryService.handle(query);

        return patientThreshold
                .map(threshold -> ResponseEntity.ok(PatientThresholdResourceFromEntityAssembler.toResourceFromEntity(threshold)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/senior-citizen/{seniorCitizenId}")
    @Operation(summary = "Update a patient's threshold", description = "Update a patient's threshold")
    public ResponseEntity<PatientThresholdResource> updatePatientThreshold(
            @PathVariable Long seniorCitizenId,
            @RequestBody UpdatePatientThresholdResource resource) {

        var command = UpdatePatientThresholdCommandFromResourceAssembler.toCommandFromResource(seniorCitizenId, resource);
        var updatedThreshold = thresholdCommandService.handle(command);

        return updatedThreshold
                .map(threshold -> ResponseEntity.ok(PatientThresholdResourceFromEntityAssembler.toResourceFromEntity(threshold)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all patient thresholds", description = "Get all patient thresholds")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient thresholds found")})
    public ResponseEntity<List<PatientThresholdResource>> getAllPatientThresholds() {
        var getAllPatientThresholdsQuery = new GetAllPatientThresholdQuery();
        var patientThresholds = patientThresholdQueryService.handle(getAllPatientThresholdsQuery);
        var patientThresholdsResources = patientThresholds.stream()
                .map(PatientThresholdResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(patientThresholdsResources);
    }
}