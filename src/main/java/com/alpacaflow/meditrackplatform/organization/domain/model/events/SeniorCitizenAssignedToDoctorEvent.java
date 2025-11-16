package com.alpacaflow.meditrackplatform.organization.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when a senior citizen is assigned to a doctor.
 */
@Getter
public class SeniorCitizenAssignedToDoctorEvent extends ApplicationEvent {

    private final Long doctorId;
    private final Long seniorCitizenId;
    private final Long organizationId;

    public SeniorCitizenAssignedToDoctorEvent(Object source, Long doctorId, Long seniorCitizenId, Long organizationId) {
        super(source);
        this.doctorId = doctorId;
        this.seniorCitizenId = seniorCitizenId;
        this.organizationId = organizationId;
    }
}

