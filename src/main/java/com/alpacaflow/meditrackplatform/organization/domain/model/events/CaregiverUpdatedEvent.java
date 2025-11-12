package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when a caregiver is updated.
 */
@Getter
public class CaregiverUpdatedEvent extends ApplicationEvent {

    private final Long caregiverId;
    private final Long organizationId;

    public CaregiverUpdatedEvent(Object source, Long caregiverId, Long organizationId) {
        super(source);
        this.caregiverId = caregiverId;
        this.organizationId = organizationId;
    }
}

