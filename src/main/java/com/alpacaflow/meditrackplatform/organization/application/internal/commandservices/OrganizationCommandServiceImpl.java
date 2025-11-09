package com.alpacaflow.meditrackplatform.organization.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.organization.domain.exceptions.OrganizationNotFoundException;
import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Organization;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.*;
import com.alpacaflow.meditrackplatform.organization.domain.services.OrganizationCommandService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the OrganizationCommandService interface.
 * <p>This class is responsible for handling the commands related to the Organization aggregate. It requires an OrganizationRepository.</p>
 * @see OrganizationCommandService
 * @see OrganizationRepository
 */
@Service
public class OrganizationCommandServiceImpl implements OrganizationCommandService {
    private final OrganizationRepository organizationRepository;

    /**
     * Constructor of the class.
     * @param organizationRepository the repository to be used by the class.
     */
    public OrganizationCommandServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    // inherit javadoc
    @Override
    public Long handle(CreateOrganizationCommand command) {
        var organization = new Organization(command.name(), command.type());
        
        try {
            var savedOrganization = organizationRepository.save(organization);
            savedOrganization.publishCreatedEvent();
            organizationRepository.save(savedOrganization);
            return savedOrganization.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving organization: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    public Optional<Organization> handle(UpdateOrganizationCommand command) {
        var result = organizationRepository.findById(command.organizationId());
        if (result.isEmpty()) {
            throw new OrganizationNotFoundException(command.organizationId());
        }
        
        var organizationToUpdate = result.get();
        try {
            var updatedOrganization = organizationToUpdate.updateInformation(
                    command.name(),
                    command.type()
            );
            
            var savedOrganization = organizationRepository.save(updatedOrganization);
            return Optional.of(savedOrganization);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating organization: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    public void handle(DeleteOrganizationCommand command) {
        if (!organizationRepository.existsById(command.organizationId())) {
            throw new OrganizationNotFoundException(command.organizationId());
        }
        
        try {
            var organization = organizationRepository.findById(command.organizationId()).orElseThrow();
            organization.markForDeletion();
            organizationRepository.save(organization);
            organizationRepository.deleteById(command.organizationId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting organization: %s".formatted(e.getMessage()));
        }
    }
}

