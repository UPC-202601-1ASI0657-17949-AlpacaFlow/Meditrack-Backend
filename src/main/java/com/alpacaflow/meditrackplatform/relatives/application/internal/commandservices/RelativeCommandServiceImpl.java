package com.alpacaflow.meditrackplatform.relatives.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetSeniorCitizenByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.SeniorCitizenQueryService;
import com.alpacaflow.meditrackplatform.relatives.domain.model.aggregates.Relative;
import com.alpacaflow.meditrackplatform.relatives.domain.model.commands.CreateRelativeCommand;
import com.alpacaflow.meditrackplatform.relatives.domain.model.valueobjects.PlanType;
import com.alpacaflow.meditrackplatform.relatives.domain.services.RelativeCommandService;
import com.alpacaflow.meditrackplatform.relatives.infrastructure.persistence.repositories.RelativeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

/**
 * Implementation of the RelativeCommandService interface.
 */
@Service
public class RelativeCommandServiceImpl implements RelativeCommandService {
    private final RelativeRepository relativeRepository;
    private final SeniorCitizenQueryService seniorCitizenQueryService;

    public RelativeCommandServiceImpl(RelativeRepository relativeRepository, SeniorCitizenQueryService seniorCitizenQueryService) {
        this.relativeRepository = relativeRepository;
        this.seniorCitizenQueryService = seniorCitizenQueryService;
    }

    @Override
    @Transactional
    public Relative handle(CreateRelativeCommand command) {
        // Get the senior citizen from the organization bounded context
        var getSeniorCitizenByIdQuery = new GetSeniorCitizenByIdQuery(command.seniorCitizenId());
        var organizationSeniorCitizen = seniorCitizenQueryService.handle(getSeniorCitizenByIdQuery)
                .orElseThrow(() -> new IllegalArgumentException("Senior citizen with id %d not found".formatted(command.seniorCitizenId())));

        // Validate that the senior citizen belongs to a relative (organizationId should be for "Individual Users")
        // We can check by organization type or name
        var organization = organizationSeniorCitizen.getOrganization();
        if (organization == null || !"relative".equals(organization.getType())) {
            throw new IllegalArgumentException("Senior citizen with id %d does not belong to a relative organization".formatted(command.seniorCitizenId()));
        }

        // Convert organization SeniorCitizen to relatives bounded context SeniorCitizen entity
        // Format birthDate as string (ISO format)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthDateStr = dateFormat.format(organizationSeniorCitizen.getBirthDate());

        var relativesSeniorCitizen = new com.alpacaflow.meditrackplatform.relatives.domain.model.entities.SeniorCitizen(
                organizationSeniorCitizen.getFirstName(),
                organizationSeniorCitizen.getLastName(),
                organizationSeniorCitizen.getDni(),
                organizationSeniorCitizen.getGender(),
                organizationSeniorCitizen.getHeight().floatValue(),
                birthDateStr,
                organizationSeniorCitizen.getWeight().floatValue(),
                organizationSeniorCitizen.getImageUrl(),
                organizationSeniorCitizen.getDeviceId().toString()
        );

        // Parse planType
        PlanType planType;
        try {
            planType = PlanType.valueOf(command.planType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid plan type: %s. Must be FREEMIUM or PREMIUM".formatted(command.planType()));
        }

        // Create the Relative
        Relative relative;
        if (command.userId() != null && command.userId() > 0) {
            relative = new Relative(
                    command.firstName(),
                    command.lastName(),
                    command.phoneNumber(),
                    command.userId(),
                    relativesSeniorCitizen
            );
        } else {
            relative = new Relative(
                    command.firstName(),
                    command.lastName(),
                    command.phoneNumber(),
                    relativesSeniorCitizen
            );
        }

        // Set plan type
        relative.setPlan(planType);

        try {
            var savedRelative = relativeRepository.save(relative);
            // Note: Relative doesn't have publishCreatedEvent() method yet
            // If domain events are needed, add the method to Relative aggregate
            return savedRelative;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving relative: %s".formatted(e.getMessage()));
        }
    }
}

