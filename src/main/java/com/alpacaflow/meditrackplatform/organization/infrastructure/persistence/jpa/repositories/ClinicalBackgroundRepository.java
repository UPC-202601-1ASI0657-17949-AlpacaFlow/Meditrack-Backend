package com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.organization.domain.model.entities.ClinicalBackground;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicalBackgroundRepository extends JpaRepository<ClinicalBackground, Long> {

    Optional<ClinicalBackground> findBySeniorCitizen_Id(Long seniorCitizenId);

    void deleteBySeniorCitizen_Id(Long seniorCitizenId);
}
