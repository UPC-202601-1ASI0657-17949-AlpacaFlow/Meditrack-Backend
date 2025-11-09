package com.alpacaflow.meditrackplatform.organization.domain.model.entities;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Doctor;
import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.SeniorCitizen;
import com.alpacaflow.meditrackplatform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Entity representing the assignment relationship between a Doctor and a SeniorCitizen.
 * This entity is part of the Doctor aggregate and represents the many-to-many relationship
 * through a junction table (Doctor_assignments).
 */
@Entity
@Table(name = "doctor_assignments")
@Getter
public class DoctorAssignment extends AuditableModel {

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "senior_citizen_id", nullable = false)
    @NotNull
    private SeniorCitizen seniorCitizen;

    public DoctorAssignment() {
        // JPA
    }

    public DoctorAssignment(Doctor doctor, SeniorCitizen seniorCitizen) {
        this.doctor = doctor;
        this.seniorCitizen = seniorCitizen;
    }
}

