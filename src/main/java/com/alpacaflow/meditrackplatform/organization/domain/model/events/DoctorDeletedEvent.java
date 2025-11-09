package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when a doctor is deleted.
 */
@Getter
public class DoctorDeletedEvent extends ApplicationEvent {

    private final Long doctorId;
    private final Long organizationId;

    public DoctorDeletedEvent(Object source, Long doctorId, Long organizationId) {
        super(source);
        this.doctorId = doctorId;
        this.organizationId = organizationId;
    }
}

