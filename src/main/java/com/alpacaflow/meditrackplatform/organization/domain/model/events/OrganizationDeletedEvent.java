package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when an organization is deleted.
 */
@Getter
public class OrganizationDeletedEvent extends ApplicationEvent {

    private final Long organizationId;

    public OrganizationDeletedEvent(Object source, Long organizationId) {
        super(source);
        this.organizationId = organizationId;
    }
}

