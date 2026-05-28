package com.alpacaflow.meditrackplatform.clinical.interfaces.rest;

import com.alpacaflow.meditrackplatform.clinical.domain.model.queries.GetPatientThresholdBySeniorCitizenIdQuery;
import com.alpacaflow.meditrackplatform.clinical.domain.services.PatientThresholdCommandService;
import com.alpacaflow.meditrackplatform.clinical.domain.services.PatientThresholdQueryService;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.CreatePatientThresholdResource;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.PatientThresholdResource;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.UpdatePatientThresholdResource;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform.CreatePatientThresholdCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform.PatientThresholdResourceFromEntityAssembler;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform.UpdatePatientThresholdCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/patient-thresholds", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Patient Thresholds", description = "Endpoints de gestión de umbrales clínicos por paciente")
public class PatientThresholdsController {

    private final PatientThresholdCommandService thresholdCommandService;
    private final PatientThresholdQueryService thresholdQueryService;

    public PatientThresholdsController(PatientThresholdCommandService thresholdCommandService, PatientThresholdQueryService thresholdQueryService) {
        this.thresholdCommandService = thresholdCommandService;
        this.thresholdQueryService = thresholdQueryService;
    }

    @PostMapping
    public ResponseEntity<PatientThresholdResource> createPatientThreshold(@RequestBody CreatePatientThresholdResource resource) {
        var command = CreatePatientThresholdCommandFromResourceAssembler.toCommandFromResource(resource);
        var patientThreshold = thresholdCommandService.handle(command);

        return patientThreshold
                .map(threshold -> new ResponseEntity<>(PatientThresholdResourceFromEntityAssembler.toResourceFromEntity(threshold), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/senior-citizen/{seniorCitizenId}")
    public ResponseEntity<PatientThresholdResource> getThresholdBySeniorCitizenId(@PathVariable Long seniorCitizenId) {
        var query = new GetPatientThresholdBySeniorCitizenIdQuery(seniorCitizenId);
        var patientThreshold = thresholdQueryService.handle(query);

        return patientThreshold
                .map(threshold -> ResponseEntity.ok(PatientThresholdResourceFromEntityAssembler.toResourceFromEntity(threshold)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/senior-citizen/{seniorCitizenId}")
    public ResponseEntity<PatientThresholdResource> updatePatientThreshold(
            @PathVariable Long seniorCitizenId,
            @RequestBody UpdatePatientThresholdResource resource) {

        var command = UpdatePatientThresholdCommandFromResourceAssembler.toCommandFromResource(seniorCitizenId, resource);
        var updatedThreshold = thresholdCommandService.handle(command);

        return updatedThreshold
                .map(threshold -> ResponseEntity.ok(PatientThresholdResourceFromEntityAssembler.toResourceFromEntity(threshold)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}