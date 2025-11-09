package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Doctor;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.DoctorResource;

/**
 * Assembler to convert a Doctor entity to a DoctorResource.
 */
public class DoctorResourceFromEntityAssembler {
    /**
     * Converts a Doctor entity to a DoctorResource.
     *
     * @param entity The {@link Doctor} entity to convert.
     * @return The {@link DoctorResource} resource that results from the conversion.
     */
    public static DoctorResource toResourceFromEntity(Doctor entity) {
        return new DoctorResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAge(),
                entity.getEmail(),
                entity.getSpecialty(),
                entity.getPhoneNumber(),
                entity.getImageUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

