package com.alpacaflow.meditrackplatform.clinical.infraestructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findBySeniorCitizenId(Long seniorCitizenId);
    boolean existsBySeniorCitizenId(Long seniorCitizenId);
}