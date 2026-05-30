package com.alpacaflow.meditrackplatform.clinical.domain.services;

import com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates.MedicalRecord;
import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.CreateMedicalRecordCommand;
import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.UpdateMedicalRecordCommand;
import java.util.Optional;

public interface MedicalRecordCommandService {
    Optional<MedicalRecord> handle(CreateMedicalRecordCommand command);
    Optional<MedicalRecord> handle(UpdateMedicalRecordCommand command);
}