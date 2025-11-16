package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when an organization is updated.
 */
@Getter
public class OrganizationUpdatedEvent extends ApplicationEvent {

    private final Long organizationId;

    public OrganizationUpdatedEvent(Object source, Long organizationId) {
        super(source);
        this.organizationId = organizationId;
    }
}

