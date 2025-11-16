package com.alpacaflow.meditrackplatform.relatives.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrackplatform.relatives.interfaces.rest.resources.RelativeResource;

public class RelativeResourceFromEntityAssembler {
    public static RelativeResource toResourceFromEntity(Relative entity) {
        return new RelativeResource(
                entity.getId(),
                entity.getPlan(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhoneNumber(),
                SeniorCitizenResourceFromEntityAssembler.toResourceFromEntity(entity.getSeniorCitizen())
        );
    }
}
