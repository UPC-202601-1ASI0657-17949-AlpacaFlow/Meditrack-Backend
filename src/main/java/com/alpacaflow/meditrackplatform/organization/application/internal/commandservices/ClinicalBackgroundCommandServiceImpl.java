package com.alpacaflow.meditrackplatform.organization.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.organization.domain.exceptions.SeniorCitizenNotFoundException;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.UpsertClinicalBackgroundCommand;
import com.alpacaflow.meditrackplatform.organization.domain.model.entities.ClinicalBackground;
import com.alpacaflow.meditrackplatform.organization.domain.services.ClinicalBackgroundCommandService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.ClinicalBackgroundRepository;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.SeniorCitizenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClinicalBackgroundCommandServiceImpl implements ClinicalBackgroundCommandService {

    private final SeniorCitizenRepository seniorCitizenRepository;
    private final ClinicalBackgroundRepository clinicalBackgroundRepository;

    public ClinicalBackgroundCommandServiceImpl(
            SeniorCitizenRepository seniorCitizenRepository,
            ClinicalBackgroundRepository clinicalBackgroundRepository
    ) {
        this.seniorCitizenRepository = seniorCitizenRepository;
        this.clinicalBackgroundRepository = clinicalBackgroundRepository;
    }

    @Override
    @Transactional
    public void handle(UpsertClinicalBackgroundCommand command) {
        var senior = seniorCitizenRepository.findById(command.seniorCitizenId())
                .orElseThrow(() -> new SeniorCitizenNotFoundException(command.seniorCitizenId()));

        var existing = clinicalBackgroundRepository.findBySeniorCitizen_Id(command.seniorCitizenId());
        if (existing.isPresent()) {
            var row = existing.get();
            row.replaceContent(
                    command.hypertension(),
                    command.diabetes(),
                    command.cardiovascularDisease(),
                    command.respiratoryDisease(),
                    command.allergies(),
                    command.medications(),
                    command.mobilityNotes(),
                    command.cognitiveCondition(),
                    command.generalNotes()
            );
            clinicalBackgroundRepository.save(row);
            return;
        }

        clinicalBackgroundRepository.save(new ClinicalBackground(
                senior,
                command.hypertension(),
                command.diabetes(),
                command.cardiovascularDisease(),
                command.respiratoryDisease(),
                command.allergies(),
                command.medications(),
                command.mobilityNotes(),
                command.cognitiveCondition(),
                command.generalNotes(),
                command.authorRole(),
                command.authorId() != null ? command.authorId() : 0L
        ));
    }
}
