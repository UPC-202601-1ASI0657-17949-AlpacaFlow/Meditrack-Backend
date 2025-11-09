package com.alpacaflow.meditrackplatform.organization.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.organization.domain.exceptions.SeniorCitizenNotFoundException;
import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.SeniorCitizen;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.*;
import com.alpacaflow.meditrackplatform.organization.domain.model.entities.DoctorAssignment;
import com.alpacaflow.meditrackplatform.organization.domain.services.SeniorCitizenCommandService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.DoctorAssignmentRepository;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.DoctorRepository;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.OrganizationRepository;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.SeniorCitizenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the SeniorCitizenCommandService interface.
 * <p>This class is responsible for handling the commands related to the SeniorCitizen aggregate. It requires a SeniorCitizenRepository, OrganizationRepository, and DoctorRepository.</p>
 * @see SeniorCitizenCommandService
 * @see SeniorCitizenRepository
 */
@Service
public class SeniorCitizenCommandServiceImpl implements SeniorCitizenCommandService {
    private final SeniorCitizenRepository seniorCitizenRepository;
    private final OrganizationRepository organizationRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorAssignmentRepository doctorAssignmentRepository;

    /**
     * Constructor of the class.
     * @param seniorCitizenRepository the repository to be used by the class.
     * @param organizationRepository the organization repository to be used by the class.
     * @param doctorRepository the doctor repository to be used by the class.
     * @param doctorAssignmentRepository the doctor assignment repository to be used by the class.
     */
    public SeniorCitizenCommandServiceImpl(SeniorCitizenRepository seniorCitizenRepository,
                                          OrganizationRepository organizationRepository,
                                          DoctorRepository doctorRepository,
                                          DoctorAssignmentRepository doctorAssignmentRepository) {
        this.seniorCitizenRepository = seniorCitizenRepository;
        this.organizationRepository = organizationRepository;
        this.doctorRepository = doctorRepository;
        this.doctorAssignmentRepository = doctorAssignmentRepository;
    }

    // inherit javadoc
    @Override
    @Transactional
    public Long handle(CreateSeniorCitizenCommand command) {
        var organization = organizationRepository.findById(command.organizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization with id %d not found".formatted(command.organizationId())));
        
        var seniorCitizen = new SeniorCitizen(
                organization,
                command.firstName(),
                command.lastName(),
                command.birthDate(),
                command.gender(),
                command.weight(),
                command.dni(),
                command.height(),
                command.imageUrl(),
                command.deviceId()
        );
        
        try {
            var savedSeniorCitizen = seniorCitizenRepository.save(seniorCitizen);
            savedSeniorCitizen.publishCreatedEvent();
            seniorCitizenRepository.save(savedSeniorCitizen);
            return savedSeniorCitizen.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving senior citizen: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    @Transactional
    public Optional<SeniorCitizen> handle(UpdateSeniorCitizenCommand command) {
        var result = seniorCitizenRepository.findById(command.seniorCitizenId());
        if (result.isEmpty()) {
            throw new SeniorCitizenNotFoundException(command.seniorCitizenId());
        }
        
        var seniorCitizenToUpdate = result.get();
        try {
            var updatedSeniorCitizen = seniorCitizenToUpdate.updatePersonalInformation(
                    command.firstName(),
                    command.lastName(),
                    command.birthDate(),
                    command.gender(),
                    command.weight(),
                    command.dni(),
                    command.height(),
                    command.imageUrl()
            );
            
            if (!command.deviceId().equals(seniorCitizenToUpdate.getDeviceId())) {
                updatedSeniorCitizen.updateDeviceId(command.deviceId());
            }
            
            var savedSeniorCitizen = seniorCitizenRepository.save(updatedSeniorCitizen);
            return Optional.of(savedSeniorCitizen);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating senior citizen: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    @Transactional
    public void handle(DeleteSeniorCitizenCommand command) {
        if (!seniorCitizenRepository.existsById(command.seniorCitizenId())) {
            throw new SeniorCitizenNotFoundException(command.seniorCitizenId());
        }
        
        try {
            var seniorCitizen = seniorCitizenRepository.findById(command.seniorCitizenId()).orElseThrow();
            seniorCitizen.markForDeletion();
            seniorCitizenRepository.save(seniorCitizen);
            seniorCitizenRepository.deleteById(command.seniorCitizenId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting senior citizen: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    @Transactional
    public Optional<SeniorCitizen> handle(AssignSeniorCitizenToDoctorCommand command) {
        var seniorCitizen = seniorCitizenRepository.findById(command.seniorCitizenId())
                .orElseThrow(() -> new SeniorCitizenNotFoundException(command.seniorCitizenId()));
        
        var doctor = doctorRepository.findById(command.doctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor with id %d not found".formatted(command.doctorId())));
        
        // Use domain logic to assign (includes validation and exclusión mutua)
        seniorCitizen.assignToDoctor(doctor.getId(), doctor.getOrganizationId());
        
        // Also publish event from doctor's perspective
        doctor.assignToSenior(seniorCitizen.getId(), seniorCitizen.getOrganizationId());
        
        try {
            // Save senior citizen (updates assignedDoctorId)
            var savedSeniorCitizen = seniorCitizenRepository.save(seniorCitizen);
            doctorRepository.save(doctor);
            
            // Persist in Doctor_assignments table
            var existingAssignment = doctorAssignmentRepository.findBySeniorCitizenId(command.seniorCitizenId());
            if (existingAssignment.isPresent()) {
                // Delete existing assignment first (since senior_citizen_id is PK, we need to delete and recreate)
                doctorAssignmentRepository.deleteBySeniorCitizenId(command.seniorCitizenId());
            }
            // Create new assignment
            var assignment = new DoctorAssignment(doctor, savedSeniorCitizen);
            doctorAssignmentRepository.save(assignment);
            
            return Optional.of(savedSeniorCitizen);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while assigning senior citizen to doctor: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    @Transactional
    public Optional<SeniorCitizen> handle(UnassignSeniorCitizenFromDoctorCommand command) {
        var seniorCitizen = seniorCitizenRepository.findById(command.seniorCitizenId())
                .orElseThrow(() -> new SeniorCitizenNotFoundException(command.seniorCitizenId()));
        
        var doctor = doctorRepository.findById(command.doctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor with id %d not found".formatted(command.doctorId())));
        
        // Use domain logic to unassign
        seniorCitizen.unassignFromDoctor(doctor.getId(), doctor.getOrganizationId());
        
        // Also publish event from doctor's perspective
        doctor.unassignFromSenior(seniorCitizen.getId(), seniorCitizen.getOrganizationId());
        
        try {
            // Save senior citizen (updates assignedDoctorId to null)
            var savedSeniorCitizen = seniorCitizenRepository.save(seniorCitizen);
            doctorRepository.save(doctor);
            
            // Remove from Doctor_assignments table
            doctorAssignmentRepository.deleteBySeniorCitizenId(command.seniorCitizenId());
            
            return Optional.of(savedSeniorCitizen);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while unassigning senior citizen from doctor: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    public Optional<SeniorCitizen> handle(AssignSeniorCitizenToCaregiverCommand command) {
        // Validate senior citizen exists
        if (!seniorCitizenRepository.existsById(command.seniorCitizenId())) {
            throw new SeniorCitizenNotFoundException(command.seniorCitizenId());
        }
        
        // Note: Caregiver repository would be needed here, but for now we'll handle it similarly
        // The actual caregiver assignment logic would be similar to doctor assignment
        // This is a placeholder that needs to be implemented when Caregiver is fully implemented
        
        throw new UnsupportedOperationException("Caregiver assignment not yet implemented");
    }

    // inherit javadoc
    @Override
    public Optional<SeniorCitizen> handle(UnassignSeniorCitizenFromCaregiverCommand command) {
        // Validate senior citizen exists
        if (!seniorCitizenRepository.existsById(command.seniorCitizenId())) {
            throw new SeniorCitizenNotFoundException(command.seniorCitizenId());
        }
        
        // Note: Caregiver repository would be needed here, but for now we'll handle it similarly
        // The actual caregiver unassignment logic would be similar to doctor unassignment
        // This is a placeholder that needs to be implemented when Caregiver is fully implemented
        
        throw new UnsupportedOperationException("Caregiver unassignment not yet implemented");
    }
}

