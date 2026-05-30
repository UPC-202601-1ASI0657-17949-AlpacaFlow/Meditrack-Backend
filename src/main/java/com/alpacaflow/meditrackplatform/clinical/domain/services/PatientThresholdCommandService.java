package com.alpacaflow.meditrackplatform.clinical.domain.services;

import com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates.PatientThreshold;
import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.CreatePatientThresholdCommand;
import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.UpdatePatientThresholdCommand;
import java.util.Optional;

public interface PatientThresholdCommandService {
    Optional<PatientThreshold> handle(CreatePatientThresholdCommand command);
    Optional<PatientThreshold> handle(UpdatePatientThresholdCommand command);
}