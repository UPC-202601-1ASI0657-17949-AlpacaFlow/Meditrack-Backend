package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Caregiver;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.CaregiverResource;

/**
 * Assembler to convert a Caregiver entity to a CaregiverResource.
 */
public class CaregiverResourceFromEntityAssembler {
    /**
     * Converts a Caregiver entity to a CaregiverResource.
     *
     * @param entity The {@link Caregiver} entity to convert.
     * @return The {@link CaregiverResource} resource that results from the conversion.
     */
    public static CaregiverResource toResourceFromEntity(Caregiver entity) {
        return new CaregiverResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAge(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getImageUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

