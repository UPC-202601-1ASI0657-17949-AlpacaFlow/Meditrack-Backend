package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when a senior citizen is created.
 */
@Getter
public class SeniorCitizenCreatedEvent extends ApplicationEvent {

    private final Long seniorCitizenId;
    private final Long organizationId;

    public SeniorCitizenCreatedEvent(Object source, Long seniorCitizenId, Long organizationId) {
        super(source);
        this.seniorCitizenId = seniorCitizenId;
        this.organizationId = organizationId;
    }
}

