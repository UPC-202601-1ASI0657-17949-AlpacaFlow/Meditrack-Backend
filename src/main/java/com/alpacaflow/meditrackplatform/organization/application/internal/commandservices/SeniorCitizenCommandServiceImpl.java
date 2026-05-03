package com.alpacaflow.meditrackplatform.organization.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.organization.application.internal.outboundservices.acl.ExternalDeviceService;
import com.alpacaflow.meditrackplatform.organization.domain.exceptions.CaregiverNotFoundException;
import com.alpacaflow.meditrackplatform.organization.domain.exceptions.SeniorCitizenDuplicateRegistrationException;
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
    private final ExternalDeviceService externalDeviceService;

    /**
     * Constructor of the class.
     * @param seniorCitizenRepository the repository to be used by the class.
     * @param organizationRepository the organization repository to be used by the class.
     * @param doctorRepository the doctor repository to be used by the class.
     * @param doctorAssignmentRepository the doctor assignment repository to be used by the class.
     * @param caregiverRepository the caregiver repository to be used by the class.
     * @param caregiverAssignmentRepository the caregiver assignment repository to be used by the class.
     * @param externalDeviceService the external device service (ACL) to create devices.
     */
    public SeniorCitizenCommandServiceImpl(SeniorCitizenRepository seniorCitizenRepository,
                                          OrganizationRepository organizationRepository,
                                          DoctorRepository doctorRepository,
                                          DoctorAssignmentRepository doctorAssignmentRepository,
                                          CaregiverRepository caregiverRepository,
                                          CaregiverAssignmentRepository caregiverAssignmentRepository,
                                          ExternalDeviceService externalDeviceService) {
        this.seniorCitizenRepository = seniorCitizenRepository;
        this.organizationRepository = organizationRepository;
        this.doctorRepository = doctorRepository;
        this.doctorAssignmentRepository = doctorAssignmentRepository;
        this.caregiverRepository = caregiverRepository;
        this.caregiverAssignmentRepository = caregiverAssignmentRepository;
        this.externalDeviceService = externalDeviceService;
    }

    // inherit javadoc
    @Override
    @Transactional
    public Long handle(CreateSeniorCitizenCommand command) {
        // Handle special case: organizationId = 0 for relatives (individual users)
        // Create or get a default organization for relatives
        var organization = getOrCreateDefaultRelativeOrganization(command.organizationId());

        assertNoDuplicateSeniorCitizenInOrganization(
                organization.getId(),
                command.firstName(),
                command.lastName(),
                command.dni(),
                null);
        
        // Determine final device ID BEFORE creating senior citizen
        Long finalDeviceId = command.deviceId();
        
        if (finalDeviceId == null || finalDeviceId == 0L) {
            // Case 1: No deviceId provided - create device with temporary holder ID (0)
            var deviceIdOptional = externalDeviceService.createDeviceForSeniorCitizen(0L);
            if (deviceIdOptional.isEmpty()) {
                throw new IllegalArgumentException("Failed to create device for senior citizen");
            }
            finalDeviceId = deviceIdOptional.get();
        } else {
            // Case 2: DeviceId provided - validate existence
            if (!externalDeviceService.deviceExists(finalDeviceId)) {
                // Device doesn't exist - create new one with temporary holder ID (0)
                var deviceIdOptional = externalDeviceService.createDeviceForSeniorCitizen(0L);
                if (deviceIdOptional.isEmpty()) {
                    throw new IllegalArgumentException("Failed to create device for senior citizen");
                }
                finalDeviceId = deviceIdOptional.get();
            }
            // If device exists, use it as is
        }

        if (command.deviceId() != null && command.deviceId() > 0 && finalDeviceId.equals(command.deviceId())) {
            assertDeviceNotLinkedToAnotherSenior(finalDeviceId, null);
        }

        // Now create the senior citizen with the valid device ID
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
                finalDeviceId
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
        assertNoDuplicateSeniorCitizenInOrganization(
                seniorCitizenToUpdate.getOrganizationId(),
                command.firstName(),
                command.lastName(),
                command.dni(),
                command.seniorCitizenId());
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
                assertDeviceNotLinkedToAnotherSenior(command.deviceId(), command.seniorCitizenId());
                updatedSeniorCitizen.updateDeviceId(command.deviceId());
            }
            
            var savedSeniorCitizen = seniorCitizenRepository.save(updatedSeniorCitizen);
            return Optional.of(savedSeniorCitizen);
        } catch (SeniorCitizenDuplicateRegistrationException e) {
            throw e;
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

    /**
     * Ensures no other senior citizen in the same organization shares the same DNI or the same full name
     * (first + last, case-insensitive).
     *
     * @param excludeSeniorCitizenId when not null, that row is ignored (for updates)
     */
    /**
     * Ensures the device is not already assigned to another senior citizen (device_id is globally unique).
     *
     * @param deviceId                device to link
     * @param excludeSeniorCitizenId when updating, the senior being edited (may keep same device); null on create
     */
    private void assertDeviceNotLinkedToAnotherSenior(Long deviceId, Long excludeSeniorCitizenId) {
        if (deviceId == null || deviceId <= 0) {
            return;
        }
        boolean taken = excludeSeniorCitizenId == null
                ? seniorCitizenRepository.existsByDeviceId(deviceId)
                : seniorCitizenRepository.existsByDeviceIdAndIdNot(deviceId, excludeSeniorCitizenId);
        if (taken) {
            throw new SeniorCitizenDuplicateRegistrationException(
                    SeniorCitizenDuplicateRegistrationException.CODE_DEVICE_ALREADY_ASSIGNED,
                    "Device %d is already assigned to another senior citizen".formatted(deviceId));
        }
    }

    private void assertNoDuplicateSeniorCitizenInOrganization(
            Long organizationId,
            String firstName,
            String lastName,
            String dni,
            Long excludeSeniorCitizenId) {
        var normalizedFirst = firstName == null ? "" : firstName.trim();
        var normalizedLast = lastName == null ? "" : lastName.trim();
        var normalizedDni = dni == null ? "" : dni.trim();

        boolean duplicateDni;
        boolean duplicateFullName;
        if (excludeSeniorCitizenId == null) {
            duplicateDni = seniorCitizenRepository.existsByOrganization_IdAndDni(organizationId, normalizedDni);
            duplicateFullName = seniorCitizenRepository.existsByOrganization_IdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(
                    organizationId, normalizedFirst, normalizedLast);
        } else {
            duplicateDni = seniorCitizenRepository.existsByOrganization_IdAndDniAndIdNot(
                    organizationId, normalizedDni, excludeSeniorCitizenId);
            duplicateFullName = seniorCitizenRepository.existsByOrganization_IdAndFirstNameIgnoreCaseAndLastNameIgnoreCaseAndIdNot(
                    organizationId, normalizedFirst, normalizedLast, excludeSeniorCitizenId);
        }

        if (duplicateDni) {
            throw new SeniorCitizenDuplicateRegistrationException(
                    SeniorCitizenDuplicateRegistrationException.CODE_DUPLICATE_DNI,
                    "Another senior citizen in this organization already has this DNI.");
        }
        if (duplicateFullName) {
            throw new SeniorCitizenDuplicateRegistrationException(
                    SeniorCitizenDuplicateRegistrationException.CODE_DUPLICATE_FULL_NAME,
                    "Another senior citizen in this organization already has this full name.");
        }
    }
}

