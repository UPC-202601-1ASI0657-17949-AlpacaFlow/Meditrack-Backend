package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.commands.UpsertClinicalBackgroundCommand;

public interface ClinicalBackgroundCommandService {
    void handle(UpsertClinicalBackgroundCommand command);
}
