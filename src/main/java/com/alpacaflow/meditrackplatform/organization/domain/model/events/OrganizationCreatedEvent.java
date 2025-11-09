package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when an organization is created.
 */
@Getter
public class OrganizationCreatedEvent extends ApplicationEvent {

    private final Long organizationId;

    public OrganizationCreatedEvent(Object source, Long organizationId) {
        super(source);
        this.organizationId = organizationId;
    }
}

