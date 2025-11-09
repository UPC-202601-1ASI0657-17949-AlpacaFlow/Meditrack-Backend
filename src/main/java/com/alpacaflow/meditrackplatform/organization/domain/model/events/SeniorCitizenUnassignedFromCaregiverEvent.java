package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when a senior citizen is unassigned from a caregiver.
 */
@Getter
public class SeniorCitizenUnassignedFromCaregiverEvent extends ApplicationEvent {

    private final Long seniorCitizenId;
    private final Long caregiverId;
    private final Long organizationId;

    public SeniorCitizenUnassignedFromCaregiverEvent(Object source, Long seniorCitizenId, Long caregiverId, Long organizationId) {
        super(source);
        this.seniorCitizenId = seniorCitizenId;
        this.caregiverId = caregiverId;
        this.organizationId = organizationId;
    }
}

