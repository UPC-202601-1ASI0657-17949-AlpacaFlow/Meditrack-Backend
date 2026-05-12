package com.alpacaflow.meditrackplatform.organization.application.internal.queryservices;

import com.alpacaflow.meditrackplatform.organization.domain.model.entities.ClinicalBackground;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetClinicalBackgroundBySeniorCitizenIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.ClinicalBackgroundQueryService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.ClinicalBackgroundRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClinicalBackgroundQueryServiceImpl implements ClinicalBackgroundQueryService {

    private final ClinicalBackgroundRepository clinicalBackgroundRepository;

    public ClinicalBackgroundQueryServiceImpl(ClinicalBackgroundRepository clinicalBackgroundRepository) {
        this.clinicalBackgroundRepository = clinicalBackgroundRepository;
    }

    @Override
    public Optional<ClinicalBackground> handle(GetClinicalBackgroundBySeniorCitizenIdQuery query) {
        return clinicalBackgroundRepository.findBySeniorCitizen_Id(query.seniorCitizenId());
    }
}
