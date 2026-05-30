package com.alpacaflow.meditrackplatform.clinical.application.internal.queryservices;

import com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates.MedicalRecord;
import com.alpacaflow.meditrackplatform.clinical.domain.model.queries.GetAllMedicalRecordsQuery;
import com.alpacaflow.meditrackplatform.clinical.domain.model.queries.GetMedicalRecordBySeniorCitizenIdQuery;
import com.alpacaflow.meditrackplatform.clinical.domain.services.MedicalRecordQueryService;
import com.alpacaflow.meditrackplatform.clinical.infraestructure.persistence.jpa.repositories.MedicalRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordQueryServiceImpl implements MedicalRecordQueryService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordQueryServiceImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicalRecord> handle(GetMedicalRecordBySeniorCitizenIdQuery query) {
        return medicalRecordRepository.findBySeniorCitizenId(query.seniorCitizenId());
    }

    @Override
    public List<MedicalRecord> handle(GetAllMedicalRecordsQuery query) {
        return medicalRecordRepository.findAll();
    }
}