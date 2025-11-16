package com.alpacaflow.meditrackplatform.organization.domain.model.entities;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Caregiver;
import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.SeniorCitizen;
import com.alpacaflow.meditrackplatform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Entity representing the assignment relationship between a Caregiver and a SeniorCitizen.
 * This entity is part of the Caregiver aggregate and represents the many-to-many relationship
 * through a junction table (Caregiver_assignments).
 */
@Entity
@Table(name = "caregiver_assignments")
@Getter
public class CaregiverAssignment extends AuditableModel {

    @ManyToOne
    @JoinColumn(name = "caregiver_id", nullable = false)
    @NotNull
    private Caregiver caregiver;

    @ManyToOne
    @JoinColumn(name = "senior_citizen_id", nullable = false)
    @NotNull
    private SeniorCitizen seniorCitizen;

    public CaregiverAssignment() {
        // JPA
    }

    public CaregiverAssignment(Caregiver caregiver, SeniorCitizen seniorCitizen) {
        this.caregiver = caregiver;
        this.seniorCitizen = seniorCitizen;
    }
}

