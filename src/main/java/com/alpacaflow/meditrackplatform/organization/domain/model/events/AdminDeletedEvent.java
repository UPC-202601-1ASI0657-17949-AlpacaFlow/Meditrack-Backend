package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when an admin is deleted.
 */
@Getter
public class AdminDeletedEvent extends ApplicationEvent {

    private final Long adminId;
    private final Long organizationId;

    public AdminDeletedEvent(Object source, Long adminId, Long organizationId) {
        super(source);
        this.adminId = adminId;
        this.organizationId = organizationId;
    }
}

