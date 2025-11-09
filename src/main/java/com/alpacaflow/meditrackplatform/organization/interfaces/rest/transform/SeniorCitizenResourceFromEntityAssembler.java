package com.alpacaflow.meditrackplatform.organization.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.SeniorCitizen;
import com.alpacaflow.meditrackplatform.organization.interfaces.rest.resources.SeniorCitizenResource;

/**
 * Assembler to convert a SeniorCitizen entity to a SeniorCitizenResource.
 */
public class SeniorCitizenResourceFromEntityAssembler {
    /**
     * Converts a SeniorCitizen entity to a SeniorCitizenResource.
     *
     * @param entity The {@link SeniorCitizen} entity to convert.
     * @return The {@link SeniorCitizenResource} resource that results from the conversion.
     */
    public static SeniorCitizenResource toResourceFromEntity(SeniorCitizen entity) {
        return new SeniorCitizenResource(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthDate(),
                entity.getAge(),
                entity.getGender(),
                entity.getWeight(),
                entity.getDni(),
                entity.getHeight(),
                entity.getImageUrl(),
                entity.getDeviceId(),
                entity.getAssignedDoctorId(),
                entity.getAssignedCaregiverId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

