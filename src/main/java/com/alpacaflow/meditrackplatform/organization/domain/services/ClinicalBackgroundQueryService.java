package com.alpacaflow.meditrackplatform.organization.domain.services;

import com.alpacaflow.meditrackplatform.organization.domain.model.entities.ClinicalBackground;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetClinicalBackgroundBySeniorCitizenIdQuery;

import java.util.Optional;

public interface ClinicalBackgroundQueryService {
    Optional<ClinicalBackground> handle(GetClinicalBackgroundBySeniorCitizenIdQuery query);
}
