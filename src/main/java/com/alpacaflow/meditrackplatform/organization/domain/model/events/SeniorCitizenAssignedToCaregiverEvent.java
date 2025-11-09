package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when a senior citizen is assigned to a caregiver.
 */
@Getter
public class SeniorCitizenAssignedToCaregiverEvent extends ApplicationEvent {

    private final Long seniorCitizenId;
    private final Long caregiverId;
    private final Long organizationId;

    public SeniorCitizenAssignedToCaregiverEvent(Object source, Long seniorCitizenId, Long caregiverId, Long organizationId) {
        super(source);
        this.seniorCitizenId = seniorCitizenId;
        this.caregiverId = caregiverId;
        this.organizationId = organizationId;
    }
}

