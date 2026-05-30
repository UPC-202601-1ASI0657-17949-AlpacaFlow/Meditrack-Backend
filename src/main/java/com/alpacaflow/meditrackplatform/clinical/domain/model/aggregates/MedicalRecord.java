package com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates;

import com.alpacaflow.meditrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

@Getter
@Entity
@Table(name = "medical_records")
public class MedicalRecord extends AuditableAbstractAggregateRoot<MedicalRecord> {

    @Column(nullable = false, unique = true, length = 15)
    private String medicalRecordNumber;

    @Column(nullable = false, unique = true)
    private Long seniorCitizenId;

    @Column(length = 2000)
    private String medicalHistoryDescription;

    @Column(length = 1000)
    private String allergies;

    public MedicalRecord() {}

    public MedicalRecord(Long seniorCitizenId, String medicalHistoryDescription, String allergies) {
        if (seniorCitizenId == null) {
            throw new IllegalArgumentException("El ID del adulto mayor no puede ser nulo");
        }
        this.seniorCitizenId = seniorCitizenId;
        this.medicalRecordNumber = generateMedicalRecordNumber();
        this.medicalHistoryDescription = medicalHistoryDescription;
        this.allergies = allergies;
    }

    public void updateRecordDetails(String medicalHistoryDescription, String allergies) {
        this.medicalHistoryDescription = medicalHistoryDescription;
        this.allergies = allergies;
    }

    private String generateMedicalRecordNumber() {
        return "MR-" + RandomStringUtils.randomNumeric(8);
    }
}