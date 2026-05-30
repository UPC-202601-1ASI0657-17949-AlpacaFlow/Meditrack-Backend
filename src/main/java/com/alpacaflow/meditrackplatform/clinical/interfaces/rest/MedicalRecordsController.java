package com.alpacaflow.meditrackplatform.clinical.interfaces.rest;

import com.alpacaflow.meditrackplatform.clinical.domain.model.queries.GetAllMedicalRecordsQuery;
import com.alpacaflow.meditrackplatform.clinical.domain.model.queries.GetMedicalRecordBySeniorCitizenIdQuery;
import com.alpacaflow.meditrackplatform.clinical.domain.services.MedicalRecordCommandService;
import com.alpacaflow.meditrackplatform.clinical.domain.services.MedicalRecordQueryService;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.CreateMedicalRecordResource;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.MedicalRecordResource;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.resources.UpdateMedicalRecordResource;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform.CreateMedicalRecordCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform.MedicalRecordResourceFromEntityAssembler;
import com.alpacaflow.meditrackplatform.clinical.interfaces.rest.transform.UpdateMedicalRecordCommandFromResourceAssembler;
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
@RequestMapping(value = "/api/v1/medical-records", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Medical Records", description = "Endpoints de gestión de historiales médicos de los pacientes")
public class MedicalRecordsController {

    private final MedicalRecordCommandService medicalRecordCommandService;
    private final MedicalRecordQueryService medicalRecordQueryService;

    public MedicalRecordsController(MedicalRecordCommandService medicalRecordCommandService, MedicalRecordQueryService medicalRecordQueryService) {
        this.medicalRecordCommandService = medicalRecordCommandService;
        this.medicalRecordQueryService = medicalRecordQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a medical record", description = "Create a  medical record")
    public ResponseEntity<MedicalRecordResource> createMedicalRecord(@RequestBody CreateMedicalRecordResource resource) {
        var command = CreateMedicalRecordCommandFromResourceAssembler.toCommandFromResource(resource);
        var medicalRecord = medicalRecordCommandService.handle(command);

        return medicalRecord
                .map(record -> new ResponseEntity<>(MedicalRecordResourceFromEntityAssembler.toResourceFromEntity(record), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/senior-citizen/{seniorCitizenId}")
    @Operation(summary = "Get a citizen medical record", description = "Get a citizen medical record")
    public ResponseEntity<MedicalRecordResource> getMedicalRecordBySeniorCitizenId(@PathVariable Long seniorCitizenId) {
        var query = new GetMedicalRecordBySeniorCitizenIdQuery(seniorCitizenId);
        var medicalRecord = medicalRecordQueryService.handle(query);

        return medicalRecord
                .map(record -> ResponseEntity.ok(MedicalRecordResourceFromEntityAssembler.toResourceFromEntity(record)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/senior-citizen/{seniorCitizenId}")
    @Operation(summary = "Update a citizen medical record", description = "Update a citizen medical record")
    public ResponseEntity<MedicalRecordResource> updateMedicalRecord(
            @PathVariable Long seniorCitizenId,
            @RequestBody UpdateMedicalRecordResource resource) {

        var command = UpdateMedicalRecordCommandFromResourceAssembler.toCommandFromResource(seniorCitizenId, resource);
        var updatedRecord = medicalRecordCommandService.handle(command);

        return updatedRecord
                .map(record -> ResponseEntity.ok(MedicalRecordResourceFromEntityAssembler.toResourceFromEntity(record)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all medical records", description = "Get all medical records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical records found")})
    public ResponseEntity<List<MedicalRecordResource>> getAllMedicalRecords() {
        var getAllMedicalRecordQuery = new GetAllMedicalRecordsQuery();
        var medicalRecords = medicalRecordQueryService.handle(getAllMedicalRecordQuery);
        var medicalRecordsResources = medicalRecords.stream()
                .map(MedicalRecordResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(medicalRecordsResources);
    }
}