package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Admin;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.AdminResource;

/**
 * Assembler to convert an Admin entity to an AdminResource.
 */
public class AdminResourceFromEntityAssembler {
    /**
     * Converts an Admin entity to an AdminResource.
     *
     * @param entity The {@link Admin} entity to convert.
     * @return The {@link AdminResource} resource that results from the conversion.
     */
    public static AdminResource toResourceFromEntity(Admin entity) {
        return new AdminResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

