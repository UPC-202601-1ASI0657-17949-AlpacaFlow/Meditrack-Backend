package com.alpacaflow.meditrackplatform.organization.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.iam.domain.services.UserCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.exceptions.CaregiverDuplicateRegistrationException;
import com.alpacaflow.meditrackplatform.organization.domain.exceptions.CaregiverNotFoundException;
import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Caregiver;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.*;
import com.alpacaflow.meditrackplatform.organization.domain.services.CaregiverCommandService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.CaregiverRepository;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the CaregiverCommandService interface.
 * <p>This class is responsible for handling the commands related to the Caregiver aggregate. It requires a CaregiverRepository and OrganizationRepository.</p>
 * @see CaregiverCommandService
 * @see CaregiverRepository
 */
@Service
public class CaregiverCommandServiceImpl implements CaregiverCommandService {
    private final CaregiverRepository caregiverRepository;
    private final OrganizationRepository organizationRepository;
    private final UserCommandService userCommandService;

    /**
     * Constructor of the class.
     * @param caregiverRepository the repository to be used by the class.
     * @param organizationRepository the organization repository to be used by the class.
     * @param userCommandService the user command service to validate user existence and role.
     */
    public CaregiverCommandServiceImpl(CaregiverRepository caregiverRepository, OrganizationRepository organizationRepository, UserCommandService userCommandService) {
        this.caregiverRepository = caregiverRepository;
        this.organizationRepository = organizationRepository;
        this.userCommandService = userCommandService;
    }

    // inherit javadoc
    @Override
    @Transactional
    public Long handle(CreateCaregiverCommand command) {
        var organization = organizationRepository.findById(command.organizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization with id %d not found".formatted(command.organizationId())));

        assertNoDuplicateCaregiverInOrganization(
                command.organizationId(),
                command.email(),
                command.firstName(),
                command.lastName(),
                null);

        final Long userId;
        
        // If userId is not provided, create User automatically with role "caregiver"
        if (command.userId() == null || command.userId() <= 0) {
            // Check if user with this email already exists
            var existingUser = userCommandService.getUserByEmail(command.email());
            if (existingUser.isPresent()) {
                var user = existingUser.get();
                // Validate that existing user has role "caregiver"
                if (user.getRole() == null || !user.getRole().equalsIgnoreCase("caregiver")) {
                    throw new IllegalArgumentException("User with email %s already exists but does not have role 'caregiver'. Current role: %s".formatted(command.email(), user.getRole()));
                }
                userId = user.getId();
            } else {
                // Create new User with role "caregiver"
                var createUserCommand = new com.alpacaflow.meditrackplatform.iam.domain.model.commands.CreateMockUserCommand(
                        command.email(),
                        "caregiver"
                );
                var createdUser = userCommandService.handle(createUserCommand);
                userId = createdUser.getId();
            }
        } else {
            // Validate that the provided user exists and has role "caregiver"
            var user = userCommandService.getUserById(command.userId())
                    .orElseThrow(() -> new IllegalArgumentException("User with id %d not found".formatted(command.userId())));
            
            if (user.getRole() == null || !user.getRole().equalsIgnoreCase("caregiver")) {
                throw new IllegalArgumentException("User with id %d does not have role 'caregiver'. Current role: %s".formatted(command.userId(), user.getRole()));
            }
            userId = command.userId();
        }
        
        var caregiver = new Caregiver(
                organization,
                userId,
                command.firstName(),
                command.lastName(),
                command.age(),
                command.email(),
                command.phoneNumber(),
                command.imageUrl()
        );
        
        try {
            var savedCaregiver = caregiverRepository.save(caregiver);
            savedCaregiver.publishCreatedEvent();
            caregiverRepository.save(savedCaregiver);
            return savedCaregiver.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving caregiver: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    @Transactional
    public Optional<Caregiver> handle(UpdateCaregiverCommand command) {
        var result = caregiverRepository.findById(command.caregiverId());
        if (result.isEmpty()) {
            throw new CaregiverNotFoundException(command.caregiverId());
        }
        
        var caregiverToUpdate = result.get();
        var organizationId = caregiverToUpdate.getOrganization().getId();
        assertNoDuplicateCaregiverInOrganization(
                organizationId,
                command.email(),
                command.firstName(),
                command.lastName(),
                command.caregiverId());

        try {
            var updatedCaregiver = caregiverToUpdate.updatePersonalInformation(
                    command.firstName(),
                    command.lastName(),
                    command.age(),
                    command.email(),
                    command.phoneNumber()
            );
            
            if (command.imageUrl() != null && !command.imageUrl().isBlank()) {
                updatedCaregiver.updateImageUrl(command.imageUrl());
            }
            
            var savedCaregiver = caregiverRepository.save(updatedCaregiver);
            return Optional.of(savedCaregiver);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating caregiver: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    @Transactional
    public void handle(DeleteCaregiverCommand command) {
        if (!caregiverRepository.existsById(command.caregiverId())) {
            throw new CaregiverNotFoundException(command.caregiverId());
        }
        
        try {
            var caregiver = caregiverRepository.findById(command.caregiverId()).orElseThrow();
            caregiver.markForDeletion();
            caregiverRepository.save(caregiver);
            caregiverRepository.deleteById(command.caregiverId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting caregiver: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    @Transactional
    public Optional<Caregiver> handle(AssignSeniorCitizenToCaregiverCommand command) {
        var caregiver = caregiverRepository.findById(command.caregiverId())
                .orElseThrow(() -> new CaregiverNotFoundException(command.caregiverId()));
        
        // Note: The actual assignment logic is handled in SeniorCitizenCommandService
        // This method is kept for consistency but the assignment should be done through SeniorCitizenCommandService
        // which handles the full business logic including validation and exclusión mutua
        
        try {
            return Optional.of(caregiver);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while assigning senior citizen to caregiver: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    @Transactional
    public Optional<Caregiver> handle(UnassignSeniorCitizenFromCaregiverCommand command) {
        var caregiver = caregiverRepository.findById(command.caregiverId())
                .orElseThrow(() -> new CaregiverNotFoundException(command.caregiverId()));
        
        // Note: The actual unassignment logic is handled in SeniorCitizenCommandService
        // This method is kept for consistency but the unassignment should be done through SeniorCitizenCommandService
        // which handles the full business logic
        
        try {
            return Optional.of(caregiver);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while unassigning senior citizen from caregiver: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Ensures no other caregiver in the same organization shares the same email (case-insensitive)
     * or the same full name (first + last, case-insensitive).
     *
     * @param excludeCaregiverId when not null, that caregiver row is ignored (for updates)
     */
    private void assertNoDuplicateCaregiverInOrganization(
            Long organizationId,
            String email,
            String firstName,
            String lastName,
            Long excludeCaregiverId) {
        var normalizedEmail = email == null ? "" : email.trim();
        var normalizedFirst = firstName == null ? "" : firstName.trim();
        var normalizedLast = lastName == null ? "" : lastName.trim();

        boolean duplicateEmail;
        boolean duplicateFullName;
        if (excludeCaregiverId == null) {
            duplicateEmail = caregiverRepository.existsByOrganization_IdAndEmailIgnoreCase(organizationId, normalizedEmail);
            duplicateFullName = caregiverRepository.existsByOrganization_IdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(
                    organizationId, normalizedFirst, normalizedLast);
        } else {
            duplicateEmail = caregiverRepository.existsByOrganization_IdAndEmailIgnoreCaseAndIdNot(
                    organizationId, normalizedEmail, excludeCaregiverId);
            duplicateFullName = caregiverRepository.existsByOrganization_IdAndFirstNameIgnoreCaseAndLastNameIgnoreCaseAndIdNot(
                    organizationId, normalizedFirst, normalizedLast, excludeCaregiverId);
        }

        if (duplicateEmail) {
            throw new CaregiverDuplicateRegistrationException(
                    CaregiverDuplicateRegistrationException.CODE_DUPLICATE_EMAIL,
                    "Another caregiver in this organization already uses this email.");
        }
        if (duplicateFullName) {
            throw new CaregiverDuplicateRegistrationException(
                    CaregiverDuplicateRegistrationException.CODE_DUPLICATE_FULL_NAME,
                    "Another caregiver in this organization already has this full name.");
        }
    }
}

