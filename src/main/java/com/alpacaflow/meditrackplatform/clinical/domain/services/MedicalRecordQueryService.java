package com.alpacaflow.meditrackplatform.clinical.domain.services;

import com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates.MedicalRecord;
import com.alpacaflow.meditrackplatform.clinical.domain.model.queries.GetAllMedicalRecordsQuery;
import com.alpacaflow.meditrackplatform.clinical.domain.model.queries.GetMedicalRecordBySeniorCitizenIdQuery;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordQueryService {
    Optional<MedicalRecord> handle(GetMedicalRecordBySeniorCitizenIdQuery query);
    List<MedicalRecord> handle(GetAllMedicalRecordsQuery query);

}