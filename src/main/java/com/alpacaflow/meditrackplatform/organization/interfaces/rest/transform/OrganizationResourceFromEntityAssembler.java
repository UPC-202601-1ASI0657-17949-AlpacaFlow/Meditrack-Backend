package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Organization;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.OrganizationResource;

/**
 * Assembler to convert an Organization entity to an OrganizationResource.
 */
public class OrganizationResourceFromEntityAssembler {
    /**
     * Converts an Organization entity to an OrganizationResource.
     *
     * @param entity The {@link Organization} entity to convert.
     * @return The {@link OrganizationResource} resource that results from the conversion.
     */
    public static OrganizationResource toResourceFromEntity(Organization entity) {
        return new OrganizationResource(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

