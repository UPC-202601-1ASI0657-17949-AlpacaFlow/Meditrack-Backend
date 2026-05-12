package com.alpacaflow.meditrackplatform.organization.domain.model.entities;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.SeniorCitizen;
import com.alpacaflow.meditrackplatform.organization.domain.model.valueobjects.ClinicalBackgroundAuthorRole;
import com.alpacaflow.meditrackplatform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Administrative / descriptive geriatric clinical notes (1:1 with {@link SeniorCitizen}).
 * <p>Not used for alert computation or threshold sync; Monitoring stays decoupled.</p>
 */
@Getter
@Setter
@Entity
@Table(name = "clinical_backgrounds")
public class ClinicalBackground extends AuditableModel {

    /** Max length for free-text clinical fields; keep in sync with {@code schema.sql} and API validation. */
    public static final int MAX_TEXT_FIELD_LENGTH = 1000;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "senior_citizen_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SeniorCitizen seniorCitizen;

    @Column(nullable = false)
    private boolean hypertension;

    @Column(nullable = false)
    private boolean diabetes;

    @Column(name = "cardiovascular_disease", nullable = false)
    private boolean cardiovascularDisease;

    @Column(name = "respiratory_disease", nullable = false)
    private boolean respiratoryDisease;

    @Column(length = MAX_TEXT_FIELD_LENGTH)
    private String allergies;

    @Column(length = MAX_TEXT_FIELD_LENGTH)
    private String medications;

    @Column(name = "mobility_notes", length = MAX_TEXT_FIELD_LENGTH)
    private String mobilityNotes;

    @Column(name = "cognitive_condition", length = MAX_TEXT_FIELD_LENGTH)
    private String cognitiveCondition;

    @Column(name = "general_notes", length = MAX_TEXT_FIELD_LENGTH)
    private String generalNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "created_by_role", nullable = false)
    private ClinicalBackgroundAuthorRole createdByRole;

    @Column(name = "created_by_id", nullable = false)
    private Long createdById;

    protected ClinicalBackground() {
    }

    public ClinicalBackground(
            SeniorCitizen seniorCitizen,
            boolean hypertension,
            boolean diabetes,
            boolean cardiovascularDisease,
            boolean respiratoryDisease,
            String allergies,
            String medications,
            String mobilityNotes,
            String cognitiveCondition,
            String generalNotes,
            ClinicalBackgroundAuthorRole createdByRole,
            Long createdById
    ) {
        this.seniorCitizen = seniorCitizen;
        this.hypertension = hypertension;
        this.diabetes = diabetes;
        this.cardiovascularDisease = cardiovascularDisease;
        this.respiratoryDisease = respiratoryDisease;
        this.allergies = allergies;
        this.medications = medications;
        this.mobilityNotes = mobilityNotes;
        this.cognitiveCondition = cognitiveCondition;
        this.generalNotes = generalNotes;
        this.createdByRole = createdByRole;
        this.createdById = createdById;
    }

    public void replaceContent(
            boolean hypertension,
            boolean diabetes,
            boolean cardiovascularDisease,
            boolean respiratoryDisease,
            String allergies,
            String medications,
            String mobilityNotes,
            String cognitiveCondition,
            String generalNotes
    ) {
        this.hypertension = hypertension;
        this.diabetes = diabetes;
        this.cardiovascularDisease = cardiovascularDisease;
        this.respiratoryDisease = respiratoryDisease;
        this.allergies = allergies;
        this.medications = medications;
        this.mobilityNotes = mobilityNotes;
        this.cognitiveCondition = cognitiveCondition;
        this.generalNotes = generalNotes;
    }

    public Long seniorCitizenId() {
        return seniorCitizen.getId();
    }
}
