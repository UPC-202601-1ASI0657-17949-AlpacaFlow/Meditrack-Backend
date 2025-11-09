package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when an admin is updated.
 */
@Getter
public class AdminUpdatedEvent extends ApplicationEvent {

    private final Long adminId;
    private final Long organizationId;

    public AdminUpdatedEvent(Object source, Long adminId, Long organizationId) {
        super(source);
        this.adminId = adminId;
        this.organizationId = organizationId;
    }
}

