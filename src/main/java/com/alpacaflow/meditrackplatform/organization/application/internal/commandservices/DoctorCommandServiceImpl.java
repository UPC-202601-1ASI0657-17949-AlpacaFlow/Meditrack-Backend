package com.alpacaflow.meditrackplatform.organization.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.iam.domain.services.UserCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.exceptions.DoctorNotFoundException;
import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Doctor;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.*;
import com.alpacaflow.meditrackplatform.organization.domain.services.DoctorCommandService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.DoctorRepository;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the DoctorCommandService interface.
 * <p>This class is responsible for handling the commands related to the Doctor aggregate. It requires a DoctorRepository and OrganizationRepository.</p>
 * @see DoctorCommandService
 * @see DoctorRepository
 */
@Service
public class DoctorCommandServiceImpl implements DoctorCommandService {
    private final DoctorRepository doctorRepository;
    private final OrganizationRepository organizationRepository;
    private final UserCommandService userCommandService;

    /**
     * Constructor of the class.
     * @param doctorRepository the repository to be used by the class.
     * @param organizationRepository the organization repository to be used by the class.
     * @param userCommandService the user command service to validate user existence and role.
     */
    public DoctorCommandServiceImpl(DoctorRepository doctorRepository, OrganizationRepository organizationRepository, UserCommandService userCommandService) {
        this.doctorRepository = doctorRepository;
        this.organizationRepository = organizationRepository;
        this.userCommandService = userCommandService;
    }

    // inherit javadoc
    @Override
    public Long handle(CreateDoctorCommand command) {
        var organization = organizationRepository.findById(command.organizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization with id %d not found".formatted(command.organizationId())));
        
        final Long userId;
        
        // If userId is not provided, create User automatically with role "doctor"
        if (command.userId() == null || command.userId() <= 0) {
            // Check if user with this email already exists
            var existingUser = userCommandService.getUserByEmail(command.email());
            if (existingUser.isPresent()) {
                var user = existingUser.get();
                // Validate that existing user has role "doctor"
                if (user.getRole() == null || !user.getRole().equalsIgnoreCase("doctor")) {
                    throw new IllegalArgumentException("User with email %s already exists but does not have role 'doctor'. Current role: %s".formatted(command.email(), user.getRole()));
                }
                userId = user.getId();
            } else {
                // Create new User with role "doctor"
                var createUserCommand = new com.alpacaflow.meditrackplatform.iam.domain.model.commands.CreateMockUserCommand(
                        command.email(),
                        "doctor"
                );
                var createdUser = userCommandService.handle(createUserCommand);
                userId = createdUser.getId();
            }
        } else {
            // Validate that the provided user exists and has role "doctor"
            var user = userCommandService.getUserById(command.userId())
                    .orElseThrow(() -> new IllegalArgumentException("User with id %d not found".formatted(command.userId())));
            
            if (user.getRole() == null || !user.getRole().equalsIgnoreCase("doctor")) {
                throw new IllegalArgumentException("User with id %d does not have role 'doctor'. Current role: %s".formatted(command.userId(), user.getRole()));
            }
            userId = command.userId();
        }
        
        var doctor = new Doctor(
                organization,
                userId,
                command.firstName(),
                command.lastName(),
                command.age(),
                command.email(),
                command.specialty(),
                command.phoneNumber(),
                command.imageUrl()
        );
        
        try {
            var savedDoctor = doctorRepository.save(doctor);
            savedDoctor.publishCreatedEvent();
            doctorRepository.save(savedDoctor);
            return savedDoctor.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving doctor: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    public Optional<Doctor> handle(UpdateDoctorCommand command) {
        var result = doctorRepository.findById(command.doctorId());
        if (result.isEmpty()) {
            throw new DoctorNotFoundException(command.doctorId());
        }
        
        var doctorToUpdate = result.get();
        try {
            var updatedDoctor = doctorToUpdate.updatePersonalInformation(
                    command.firstName(),
                    command.lastName(),
                    command.age(),
                    command.email(),
                    command.phoneNumber()
            );
            
            if (command.specialty() != null && !command.specialty().isBlank()) {
                updatedDoctor.updateSpecialty(command.specialty());
            }
            
            if (command.imageUrl() != null && !command.imageUrl().isBlank()) {
                updatedDoctor.updateImageUrl(command.imageUrl());
            }
            
            var savedDoctor = doctorRepository.save(updatedDoctor);
            return Optional.of(savedDoctor);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating doctor: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    public void handle(DeleteDoctorCommand command) {
        if (!doctorRepository.existsById(command.doctorId())) {
            throw new DoctorNotFoundException(command.doctorId());
        }
        
        try {
            var doctor = doctorRepository.findById(command.doctorId()).orElseThrow();
            doctor.markForDeletion();
            doctorRepository.save(doctor);
            doctorRepository.deleteById(command.doctorId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting doctor: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    public Optional<Doctor> handle(AssignSeniorCitizenToDoctorCommand command) {
        var doctor = doctorRepository.findById(command.doctorId())
                .orElseThrow(() -> new DoctorNotFoundException(command.doctorId()));
        
        // Note: The actual assignment logic is handled in SeniorCitizenCommandService
        // This method is kept for consistency but the assignment should be done through SeniorCitizenCommandService
        // which handles the full business logic including validation and exclusión mutua
        
        try {
            return Optional.of(doctor);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while assigning senior citizen to doctor: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    public Optional<Doctor> handle(UnassignSeniorCitizenFromDoctorCommand command) {
        var doctor = doctorRepository.findById(command.doctorId())
                .orElseThrow(() -> new DoctorNotFoundException(command.doctorId()));
        
        // Note: The actual unassignment logic is handled in SeniorCitizenCommandService
        // This method is kept for consistency but the unassignment should be done through SeniorCitizenCommandService
        // which handles the full business logic
        
        try {
            return Optional.of(doctor);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while unassigning senior citizen from doctor: %s".formatted(e.getMessage()));
        }
    }
}

