package com.alpacaflow.meditrackplatform.organization.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.organization.domain.exceptions.CaregiverNotFoundException;
import com.alpacaflow.meditrackplatform.organization.domain.exceptions.SeniorCitizenNotFoundException;
import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Caregiver;
import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.SeniorCitizen;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.*;
import com.alpacaflow.meditrackplatform.organization.domain.model.entities.CaregiverAssignment;
import com.alpacaflow.meditrackplatform.organization.domain.model.entities.DoctorAssignment;
import com.alpacaflow.meditrackplatform.organization.domain.services.SeniorCitizenCommandService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.CaregiverAssignmentRepository;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.CaregiverRepository;
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
    private final CaregiverRepository caregiverRepository;
    private final CaregiverAssignmentRepository caregiverAssignmentRepository;

    /**
     * Constructor of the class.
     * @param seniorCitizenRepository the repository to be used by the class.
     * @param organizationRepository the organization repository to be used by the class.
     * @param doctorRepository the doctor repository to be used by the class.
     * @param doctorAssignmentRepository the doctor assignment repository to be used by the class.
     * @param caregiverRepository the caregiver repository to be used by the class.
     * @param caregiverAssignmentRepository the caregiver assignment repository to be used by the class.
     */
    public SeniorCitizenCommandServiceImpl(SeniorCitizenRepository seniorCitizenRepository,
                                          OrganizationRepository organizationRepository,
                                          DoctorRepository doctorRepository,
                                          DoctorAssignmentRepository doctorAssignmentRepository,
                                          CaregiverRepository caregiverRepository,
                                          CaregiverAssignmentRepository caregiverAssignmentRepository) {
        this.seniorCitizenRepository = seniorCitizenRepository;
        this.organizationRepository = organizationRepository;
        this.doctorRepository = doctorRepository;
        this.doctorAssignmentRepository = doctorAssignmentRepository;
        this.caregiverRepository = caregiverRepository;
        this.caregiverAssignmentRepository = caregiverAssignmentRepository;
    }

    // inherit javadoc
    @Override
    @Transactional
    public Long handle(CreateSeniorCitizenCommand command) {
        // Handle special case: organizationId = 0 for relatives (individual users)
        // Create or get a default organization for relatives
        var organization = getOrCreateDefaultRelativeOrganization(command.organizationId());
        
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

    /**
     * Gets or creates a default organization for relatives (when organizationId = 0)
     * @param organizationId the organization ID from the command (0 for relatives)
     * @return the organization to use
     */
    private com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Organization getOrCreateDefaultRelativeOrganization(Long organizationId) {
        // If organizationId is 0, use a special default organization for relatives
        if (organizationId == 0) {
            // Try to find an existing "Individual Users" organization
            // For now, we'll create one if it doesn't exist
            // In a production system, this should be created via a migration or initialization script
            var defaultOrg = organizationRepository.findAll().stream()
                    .filter(org -> "Individual Users".equals(org.getName()) && "relative".equals(org.getType()))
                    .findFirst();
            
            if (defaultOrg.isPresent()) {
                return defaultOrg.get();
            } else {
                // Create default organization for relatives
                var newOrg = new com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Organization(
                        "Individual Users", "relative");
                var savedOrg = organizationRepository.save(newOrg);
                savedOrg.publishCreatedEvent();
                organizationRepository.save(savedOrg);
                return savedOrg;
            }
        } else {
            // Normal case: find the organization by ID
            return organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new IllegalArgumentException("Organization with id %d not found".formatted(organizationId)));
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
    @Transactional
    public Optional<SeniorCitizen> handle(AssignSeniorCitizenToCaregiverCommand command) {
        var seniorCitizen = seniorCitizenRepository.findById(command.seniorCitizenId())
                .orElseThrow(() -> new SeniorCitizenNotFoundException(command.seniorCitizenId()));
        
        var caregiver = caregiverRepository.findById(command.caregiverId())
                .orElseThrow(() -> new CaregiverNotFoundException(command.caregiverId()));
        
        // Use domain logic to assign (includes validation and exclusión mutua)
        seniorCitizen.assignToCaregiver(caregiver.getId(), caregiver.getOrganizationId());
        
        // Also publish event from caregiver's perspective
        caregiver.assignToSenior(seniorCitizen.getId(), seniorCitizen.getOrganizationId());
        
        try {
            // Save senior citizen (updates assignedCaregiverId)
            var savedSeniorCitizen = seniorCitizenRepository.save(seniorCitizen);
            caregiverRepository.save(caregiver);
            
            // Persist in Caregiver_assignments table
            var existingAssignment = caregiverAssignmentRepository.findBySeniorCitizenId(command.seniorCitizenId());
            if (existingAssignment.isPresent()) {
                // Delete existing assignment first (since senior_citizen_id is PK, we need to delete and recreate)
                caregiverAssignmentRepository.deleteBySeniorCitizenId(command.seniorCitizenId());
            }
            // Create new assignment
            var assignment = new CaregiverAssignment(caregiver, savedSeniorCitizen);
            caregiverAssignmentRepository.save(assignment);
            
            return Optional.of(savedSeniorCitizen);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while assigning senior citizen to caregiver: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    @Transactional
    public Optional<SeniorCitizen> handle(UnassignSeniorCitizenFromCaregiverCommand command) {
        var seniorCitizen = seniorCitizenRepository.findById(command.seniorCitizenId())
                .orElseThrow(() -> new SeniorCitizenNotFoundException(command.seniorCitizenId()));
        
        var caregiver = caregiverRepository.findById(command.caregiverId())
                .orElseThrow(() -> new CaregiverNotFoundException(command.caregiverId()));
        
        // Use domain logic to unassign
        seniorCitizen.unassignFromCaregiver(caregiver.getId(), caregiver.getOrganizationId());
        
        // Also publish event from caregiver's perspective
        caregiver.unassignFromSenior(seniorCitizen.getId(), seniorCitizen.getOrganizationId());
        
        try {
            // Save senior citizen (updates assignedCaregiverId to null)
            var savedSeniorCitizen = seniorCitizenRepository.save(seniorCitizen);
            caregiverRepository.save(caregiver);
            
            // Remove from Caregiver_assignments table
            caregiverAssignmentRepository.deleteBySeniorCitizenId(command.seniorCitizenId());
            
            return Optional.of(savedSeniorCitizen);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while unassigning senior citizen from caregiver: %s".formatted(e.getMessage()));
        }
    }
}

