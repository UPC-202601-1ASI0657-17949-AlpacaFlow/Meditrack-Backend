package com.alpacaflow.meditrackplatform.clinical.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.clinical.domain.model.aggregates.MedicalRecord;
import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.CreateMedicalRecordCommand;
import com.alpacaflow.meditrackplatform.clinical.domain.model.commands.UpdateMedicalRecordCommand;
import com.alpacaflow.meditrackplatform.clinical.domain.services.MedicalRecordCommandService;
import com.alpacaflow.meditrackplatform.clinical.infraestructure.persistence.jpa.repositories.MedicalRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MedicalRecordCommandServiceImpl implements MedicalRecordCommandService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordCommandServiceImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    @Transactional
    public Optional<MedicalRecord> handle(CreateMedicalRecordCommand command) {
        if (medicalRecordRepository.existsBySeniorCitizenId(command.seniorCitizenId())) {
            throw new IllegalArgumentException("Ya existe un historial médico registrado para este paciente");
        }
        var medicalRecord = new MedicalRecord(
                command.seniorCitizenId(),
                command.medicalHistoryDescription(),
                command.allergies()
        );
        medicalRecordRepository.save(medicalRecord);
        return Optional.of(medicalRecord);
    }

    @Override
    @Transactional
    public Optional<MedicalRecord> handle(UpdateMedicalRecordCommand command) {
        var result = medicalRecordRepository.findBySeniorCitizenId(command.seniorCitizenId());
        if (result.isEmpty()) return Optional.empty();

        var medicalRecord = result.get();
        medicalRecord.updateRecordDetails(command.medicalHistoryDescription(), command.allergies());
        medicalRecordRepository.save(medicalRecord);

        return Optional.of(medicalRecord);
    }
}