package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when a senior citizen is deleted.
 */
@Getter
public class SeniorCitizenDeletedEvent extends ApplicationEvent {

    private final Long seniorCitizenId;
    private final Long organizationId;

    public SeniorCitizenDeletedEvent(Object source, Long seniorCitizenId, Long organizationId) {
        super(source);
        this.seniorCitizenId = seniorCitizenId;
        this.organizationId = organizationId;
    }
}

