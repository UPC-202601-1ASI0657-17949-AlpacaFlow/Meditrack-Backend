package com.alpacaflow.meditrackplatform.relatives.interfaces.rest;

import com.alpacaflow.meditrackplatform.relatives.domain.model.queries.GetRelativeByIdQuery;
import com.alpacaflow.meditrackplatform.relatives.domain.model.queries.GetRelativeByUserIdQuery;
import com.alpacaflow.meditrackplatform.relatives.domain.services.RelativeCommandService;
import com.alpacaflow.meditrackplatform.relatives.domain.services.RelativeQueryService;
import com.alpacaflow.meditrackplatform.relatives.interfaces.rest.resources.CreateRelativeResource;
import com.alpacaflow.meditrackplatform.relatives.interfaces.rest.resources.RelativeResource;
import com.alpacaflow.meditrackplatform.relatives.interfaces.rest.transform.CreateRelativeCommandFromResourceAssembler;
import com.alpacaflow.meditrackplatform.relatives.interfaces.rest.transform.RelativeResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/relatives", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Relatives", description = "Endpoints for managing relatives")
public class RelativeController {

    private final RelativeQueryService relativeQueryService;
    private final RelativeCommandService relativeCommandService;

    public RelativeController(RelativeQueryService relativeQueryService, RelativeCommandService relativeCommandService) {
        this.relativeQueryService = relativeQueryService;
        this.relativeCommandService = relativeCommandService;
    }

    @GetMapping("/{relativeId}")
    @Operation(summary = "Get relative by ID", description = "Retrieve a specific relative by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relative found"),
            @ApiResponse(responseCode = "404", description = "Relative not found")
    })
    public ResponseEntity<RelativeResource> getRelative(@PathVariable Long relativeId) {
        var getRelativeByIdQuery = new GetRelativeByIdQuery(relativeId);
        var relative = relativeQueryService.handle(getRelativeByIdQuery);
        if(relative.isEmpty()) return ResponseEntity.notFound().build();
        var relativeResource = RelativeResourceFromEntityAssembler.toResourceFromEntity(relative.get());
        return ResponseEntity.ok(relativeResource);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get relative by user ID", description = "Retrieve a specific relative by its userId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relative found"),
            @ApiResponse(responseCode = "404", description = "Relative not found")
    })
    public ResponseEntity<RelativeResource> getRelativeByUserId(@PathVariable Long userId) {
        var getRelativeByUserIdQuery = new GetRelativeByUserIdQuery(userId);
        var relative = relativeQueryService.handle(getRelativeByUserIdQuery);
        if(relative.isEmpty()) return ResponseEntity.notFound().build();
        var relativeResource = RelativeResourceFromEntityAssembler.toResourceFromEntity(relative.get());
        return ResponseEntity.ok(relativeResource);
    }

    @PostMapping
    @Operation(summary = "Create a new relative", description = "Create a new relative with the provided information")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Relative created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<RelativeResource> createRelative(@RequestBody CreateRelativeResource resource) {
        var createRelativeCommand = CreateRelativeCommandFromResourceAssembler.toCommandFromResource(resource);
        var relative = relativeCommandService.handle(createRelativeCommand);
        var relativeResource = RelativeResourceFromEntityAssembler.toResourceFromEntity(relative);
        return new ResponseEntity<>(relativeResource, HttpStatus.CREATED);
    }
}
