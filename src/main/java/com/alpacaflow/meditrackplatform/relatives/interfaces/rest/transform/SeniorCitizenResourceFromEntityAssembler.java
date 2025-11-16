package com.alpacaflow.meditrackplatform.relatives.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.relatives.domain.model.entities.SeniorCitizen;
import com.alpacaflow.meditrackplatform.relatives.interfaces.rest.resources.SeniorCitizenResource;

public class SeniorCitizenResourceFromEntityAssembler {
    public static SeniorCitizenResource toResourceFromEntity(SeniorCitizen entity){
        return new SeniorCitizenResource(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDni(),
                entity.getGender(),
                entity.getHeight(),
                entity.getBirthDate(),
                entity.getWeight(),
                entity.getProfileImage(),
                entity.getDeviceId()
        );
    }
}
