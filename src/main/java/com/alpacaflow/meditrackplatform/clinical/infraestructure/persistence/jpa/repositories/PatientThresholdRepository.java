package com.alpacaflow.meditrackplatform.clinical.infraestructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates.PatientThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientThresholdRepository extends JpaRepository<PatientThreshold, Long> {
    Optional<PatientThreshold> findBySeniorCitizenId(Long seniorCitizenId);
    boolean existsBySeniorCitizenId(Long seniorCitizenId);
}