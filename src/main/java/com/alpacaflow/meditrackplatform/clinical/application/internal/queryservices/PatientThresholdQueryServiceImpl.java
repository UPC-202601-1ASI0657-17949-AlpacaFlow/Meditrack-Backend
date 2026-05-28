package com.alpacaflow.meditrackplatform.clinical.application.internal.queryservices;

import com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates.PatientThreshold;
import com.alpacaflow.meditrackplatform.clinical.domain.model.queries.GetPatientThresholdBySeniorCitizenIdQuery;
import com.alpacaflow.meditrackplatform.clinical.domain.services.PatientThresholdQueryService;
import com.alpacaflow.meditrackplatform.clinical.infraestructure.persistence.jpa.repositories.PatientThresholdRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PatientThresholdQueryServiceImpl implements PatientThresholdQueryService {

    private final PatientThresholdRepository thresholdRepository;

    public PatientThresholdQueryServiceImpl(PatientThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PatientThreshold> handle(GetPatientThresholdBySeniorCitizenIdQuery query) {
        return thresholdRepository.findBySeniorCitizenId(query.seniorCitizenId());
    }
}