package com.alpacaflow.meditrackplatform.relatives.interfaces.rest;

import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetClinicalBackgroundBySeniorCitizenIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetSeniorCitizenByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.valueobjects.ClinicalBackgroundAuthorRole;
import com.alpacaflow.meditrackplatform.organization.domain.services.ClinicalBackgroundCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.services.ClinicalBackgroundQueryService;
import com.alpacaflow.meditrackplatform.organization.domain.services.SeniorCitizenQueryService;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.ClinicalBackgroundResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.UpsertClinicalBackgroundResource;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform.ClinicalBackgroundAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Relatives BC API surface for the same {@code clinical_backgrounds} row (Organization is source of truth).
 * <p>{@code organizationSeniorCitizenId} is the adulto mayor id in the Organization context.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/relatives/senior-citizens", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Relatives — Clinical background", description = "Family-side read/write of descriptive clinical notes")
public class RelativeClinicalBackgroundController {

    private final SeniorCitizenQueryService seniorCitizenQueryService;
    private final ClinicalBackgroundQueryService clinicalBackgroundQueryService;
    private final ClinicalBackgroundCommandService clinicalBackgroundCommandService;

    public RelativeClinicalBackgroundController(
            SeniorCitizenQueryService seniorCitizenQueryService,
            ClinicalBackgroundQueryService clinicalBackgroundQueryService,
            ClinicalBackgroundCommandService clinicalBackgroundCommandService
    ) {
        this.seniorCitizenQueryService = seniorCitizenQueryService;
        this.clinicalBackgroundQueryService = clinicalBackgroundQueryService;
        this.clinicalBackgroundCommandService = clinicalBackgroundCommandService;
    }

    @GetMapping("/{organizationSeniorCitizenId}/clinical-background")
    @PreAuthorize("hasRole('RELATIVE')")
    @Operation(summary = "Get clinical background (relative channel)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payload returned"),
            @ApiResponse(responseCode = "404", description = "Senior citizen not found in organization registry")})
    public ResponseEntity<ClinicalBackgroundResource> getClinicalBackground(
            @PathVariable Long organizationSeniorCitizenId
    ) {
        var senior = seniorCitizenQueryService.handle(new GetSeniorCitizenByIdQuery(organizationSeniorCitizenId));
        if (senior.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var clinical = clinicalBackgroundQueryService.handle(new GetClinicalBackgroundBySeniorCitizenIdQuery(organizationSeniorCitizenId));
        var resource = clinical.map(ClinicalBackgroundAssembler::toResource)
                .orElseGet(() -> ClinicalBackgroundAssembler.emptyShell(organizationSeniorCitizenId));
        return ResponseEntity.ok(resource);
    }

    @PutMapping(value = "/{organizationSeniorCitizenId}/clinical-background", consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('RELATIVE')")
    @Operation(summary = "Upsert clinical background (relative channel)", description = "Use authorRole RELATIVE. Does not affect Monitoring.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saved"),
            @ApiResponse(responseCode = "404", description = "Senior citizen not found")})
    public ResponseEntity<ClinicalBackgroundResource> upsertClinicalBackground(
            @PathVariable Long organizationSeniorCitizenId,
            @RequestBody @Valid UpsertClinicalBackgroundResource resource
    ) {
        // Relative channel always persists RELATIVE as author role (defensive against stale frontend payloads).
        var normalizedResource = new UpsertClinicalBackgroundResource(
                resource.hypertension(),
                resource.diabetes(),
                resource.cardiovascularDisease(),
                resource.respiratoryDisease(),
                resource.allergies(),
                resource.medications(),
                resource.mobilityNotes(),
                resource.cognitiveCondition(),
                resource.generalNotes(),
                ClinicalBackgroundAuthorRole.RELATIVE,
                resource.authorId()
        );
        var command = ClinicalBackgroundAssembler.toCommand(organizationSeniorCitizenId, normalizedResource);
        clinicalBackgroundCommandService.handle(command);
        var clinical = clinicalBackgroundQueryService.handle(new GetClinicalBackgroundBySeniorCitizenIdQuery(organizationSeniorCitizenId));
        return clinical.map(ClinicalBackgroundAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
