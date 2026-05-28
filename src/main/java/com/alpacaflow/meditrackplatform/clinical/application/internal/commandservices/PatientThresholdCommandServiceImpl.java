package com.alpacaflow.meditrackplatform.clinical.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates.PatientThreshold;
import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.CreatePatientThresholdCommand;
import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.UpdatePatientThresholdCommand;
import com.alpacaflow.meditrackplatform.clinical.domain.model.valueobjects.HeartRateThreshold;
import com.alpacaflow.meditrackplatform.clinical.domain.model.valueobjects.OxygenThreshold;
import com.alpacaflow.meditrackplatform.clinical.domain.model.valueobjects.TemperatureThreshold;
import com.alpacaflow.meditrackplatform.clinical.domain.services.PatientThresholdCommandService;
import com.alpacaflow.meditrackplatform.clinical.infraestructure.persistence.jpa.repositories.PatientThresholdRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PatientThresholdCommandServiceImpl implements PatientThresholdCommandService {

    private final PatientThresholdRepository thresholdRepository;

    public PatientThresholdCommandServiceImpl(PatientThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    @Override
    @Transactional
    public Optional<PatientThreshold> handle(CreatePatientThresholdCommand command) {
        if (thresholdRepository.existsBySeniorCitizenId(command.seniorCitizenId())) {
            throw new IllegalArgumentException("Ya existen umbrales configurados para este paciente");
        }
        var patientThreshold = new PatientThreshold(command.seniorCitizenId());
        thresholdRepository.save(patientThreshold);
        return Optional.of(patientThreshold);
    }

    @Override
    @Transactional
    public Optional<PatientThreshold> handle(UpdatePatientThresholdCommand command) {
        var result = thresholdRepository.findBySeniorCitizenId(command.seniorCitizenId());
        if (result.isEmpty()) return Optional.empty();

        var patientThreshold = result.get();

        // Crear nuevos value objects aplicando sus respectivas validaciones internas
        var heartRate = new HeartRateThreshold(command.minBpm(), command.maxBpm());
        var oxygen = new OxygenThreshold(command.minSpo2());
        var temperature = new TemperatureThreshold(command.minCelsius(), command.maxCelsius());

        patientThreshold.updateThresholds(heartRate, oxygen, temperature);
        thresholdRepository.save(patientThreshold);

        return Optional.of(patientThreshold);
    }
}