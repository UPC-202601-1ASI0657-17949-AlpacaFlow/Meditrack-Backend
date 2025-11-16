package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when a caregiver is deleted.
 */
@Getter
public class CaregiverDeletedEvent extends ApplicationEvent {

    private final Long caregiverId;
    private final Long organizationId;

    public CaregiverDeletedEvent(Object source, Long caregiverId, Long organizationId) {
        super(source);
        this.caregiverId = caregiverId;
        this.organizationId = organizationId;
    }
}

