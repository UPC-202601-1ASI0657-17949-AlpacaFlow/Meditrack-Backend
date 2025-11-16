package com.alpacaflow.meditrackplatform.organization.domain.model.aggregates;

import com.alpacaflow.meditrackplatform.organization.domain.model.events.SeniorCitizenAssignedToCaregiverEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.SeniorCitizenAssignedToDoctorEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.SeniorCitizenCreatedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.SeniorCitizenDeletedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.SeniorCitizenUnassignedFromCaregiverEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.SeniorCitizenUnassignedFromDoctorEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.SeniorCitizenUpdatedEvent;
import com.alpacaflow.meditrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.util.Calendar;
import java.util.Date;

/**
 * SeniorCitizen aggregate representing an elderly person within an organization.
 * Acts as part of the organization bounded context.
 * This entity ensures multi-tenant data isolation through organization relationship.
 */
@Entity
@Getter
public class SeniorCitizen extends AuditableAbstractAggregateRoot<SeniorCitizen> {

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "dni", nullable = false)
    private String dni;

    @Column(name = "height", nullable = false)
    private Double height;

    @Column(name = "profile_image", nullable = false)
    private String imageUrl;

    @Column(name = "device_id", nullable = false, unique = true)
    private Long deviceId;

    @Column(name = "assigned_doctor_id")
    private Long assignedDoctorId;

    @Column(name = "assigned_caregiver_id")
    private Long assignedCaregiverId;

    /**
     * Default constructor for JPA
     */
    public SeniorCitizen() {
        super();
    }

    /**
     * Constructor for creating a new SeniorCitizen with all required fields
     * @param organization the organization this senior citizen belongs to
     * @param firstName the senior citizen's first name
     * @param lastName the senior citizen's last name
     * @param birthDate the senior citizen's birth date
     * @param gender the senior citizen's gender
     * @param weight the senior citizen's weight
     * @param dni the senior citizen's DNI (identification number)
     * @param height the senior citizen's height
     * @param imageUrl the senior citizen's image URL
     * @param deviceId the senior citizen's device ID (unique)
     */
    public SeniorCitizen(Organization organization, String firstName, String lastName, Date birthDate,
                        String gender, Double weight, String dni, Double height, String imageUrl, Long deviceId) {
        this();
        this.organization = organization;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.age = calculateAge(birthDate);
        this.gender = gender;
        this.weight = weight;
        this.dni = dni;
        this.height = height;
        this.imageUrl = imageUrl;
        this.deviceId = deviceId;
    }

    /**
     * Publishes a SeniorCitizenCreatedEvent. This should be called after the senior citizen is persisted
     * and has an ID assigned.
     */
    public void publishCreatedEvent() {
        this.addDomainEvent(new SeniorCitizenCreatedEvent(this, this.getId(), this.getOrganizationId()));
    }

    /**
     * Updates the senior citizen's personal information
     * @param firstName the senior citizen's first name
     * @param lastName the senior citizen's last name
     * @param birthDate the senior citizen's birth date
     * @param gender the senior citizen's gender
     * @param weight the senior citizen's weight
     * @param dni the senior citizen's DNI
     * @param height the senior citizen's height
     * @param imageUrl the senior citizen's image URL
     * @return this senior citizen instance
     */
    public SeniorCitizen updatePersonalInformation(String firstName, String lastName, Date birthDate,
                                                   String gender, Double weight, String dni, Double height, String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.age = calculateAge(birthDate);
        this.gender = gender;
        this.weight = weight;
        this.dni = dni;
        this.height = height;
        this.imageUrl = imageUrl;
        this.addDomainEvent(new SeniorCitizenUpdatedEvent(this, this.getId(), this.getOrganizationId()));
        return this;
    }

    /**
     * Updates the senior citizen's device ID
     * @param deviceId the new device ID
     * @return this senior citizen instance
     */
    public SeniorCitizen updateDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        this.addDomainEvent(new SeniorCitizenUpdatedEvent(this, this.getId(), this.getOrganizationId()));
        return this;
    }

    /**
     * Gets the full name of the senior citizen
     * @return the full name (first name + last name)
     */
    public String getFullName() {
        return String.format("%s %s", firstName, lastName).trim();
    }

    /**
     * Gets the organization ID
     * @return the organization ID
     */
    public Long getOrganizationId() {
        return organization != null ? organization.getId() : null;
    }

    /**
     * Domain logic: Calculates age from birth date
     * @param birthDate the birth date
     * @return the calculated age
     */
    private Integer calculateAge(Date birthDate) {
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDate);
        Calendar today = Calendar.getInstance();
        
        int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        int monthDiff = today.get(Calendar.MONTH) - birth.get(Calendar.MONTH);
        if (monthDiff < 0 || (monthDiff == 0 && today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age;
    }

    /**
     * Domain logic: Validates that this senior citizen belongs to the same organization as the given organization ID.
     * @param organizationId the organization ID to validate against
     * @return true if this senior citizen belongs to the given organization
     */
    public boolean belongsToOrganization(Long organizationId) {
        return this.organization != null && this.organization.getId().equals(organizationId);
    }

    /**
     * Domain logic: Checks if this senior citizen can be assigned to a doctor.
     * A senior citizen can only be assigned to a doctor if it's not already assigned to any caregiver.
     * @return true if can be assigned to a doctor, false otherwise
     */
    public boolean canBeAssignedToDoctor() {
        return this.assignedCaregiverId == null;
    }

    /**
     * Domain logic: Checks if this senior citizen can be assigned to a caregiver.
     * A senior citizen can only be assigned to a caregiver if it's not already assigned to any doctor.
     * @return true if can be assigned to a caregiver, false otherwise
     */
    public boolean canBeAssignedToCaregiver() {
        return this.assignedDoctorId == null;
    }

    /**
     * Domain logic: Checks if this senior citizen is assigned to any doctor
     * @return true if assigned to a doctor
     */
    public boolean isAssignedToAnyDoctor() {
        return this.assignedDoctorId != null;
    }

    /**
     * Domain logic: Checks if this senior citizen is assigned to any caregiver
     * @return true if assigned to a caregiver
     */
    public boolean isAssignedToAnyCaregiver() {
        return this.assignedCaregiverId != null;
    }

    /**
     * Domain logic: Checks if this senior citizen is assigned to a specific doctor
     * @param doctorId the ID of the doctor to check
     * @return true if assigned to this specific doctor
     */
    public boolean isAssignedToDoctor(Long doctorId) {
        return this.assignedDoctorId != null && this.assignedDoctorId.equals(doctorId);
    }

    /**
     * Domain logic: Checks if this senior citizen is assigned to a specific caregiver
     * @param caregiverId the ID of the caregiver to check
     * @return true if assigned to this specific caregiver
     */
    public boolean isAssignedToCaregiver(Long caregiverId) {
        return this.assignedCaregiverId != null && this.assignedCaregiverId.equals(caregiverId);
    }

    /**
     * Domain logic: Checks if this senior citizen is assigned to a specific person (doctor or caregiver)
     * @param personId the ID of the person to check
     * @return true if assigned to this person
     */
    public boolean isAssignedTo(Long personId) {
        return isAssignedToDoctor(personId) || isAssignedToCaregiver(personId);
    }

    /**
     * Domain logic: Assigns this senior citizen to a doctor.
     * Validates that the senior citizen is not already assigned to a caregiver (exclusión mutua).
     * If already assigned to another doctor, it will be reassigned to the new doctor.
     * @param doctorId the ID of the doctor to assign
     * @param doctorOrganizationId the organization ID of the doctor (for validation)
     */
    public void assignToDoctor(Long doctorId, Long doctorOrganizationId) {
        // Validate same organization
        if (!belongsToOrganization(doctorOrganizationId)) {
            throw new IllegalStateException(
                String.format("Cannot assign senior citizen to doctor: They belong to different organizations " +
                    "(Senior Citizen: org %d, Doctor: org %d)", this.getOrganizationId(), doctorOrganizationId)
            );
        }

        // Validate exclusión mutua: cannot be assigned to doctor if already assigned to caregiver
        if (!canBeAssignedToDoctor()) {
            throw new IllegalStateException(
                "Cannot assign senior citizen to doctor: Senior citizen is already assigned to a caregiver. " +
                "A senior citizen can only be assigned to doctors OR caregivers, not both."
            );
        }

        // If already assigned to another doctor, unassign from previous doctor first
        Long previousDoctorId = this.assignedDoctorId;
        if (previousDoctorId != null && !previousDoctorId.equals(doctorId)) {
            this.assignedDoctorId = null;
            this.addDomainEvent(new SeniorCitizenUnassignedFromDoctorEvent(this, previousDoctorId, this.getId(), this.getOrganizationId()));
        }

        // Assign to new doctor
        this.assignedDoctorId = doctorId;
        this.assignedCaregiverId = null; // Ensure exclusión mutua
        this.addDomainEvent(new SeniorCitizenAssignedToDoctorEvent(this, doctorId, this.getId(), this.getOrganizationId()));
    }

    /**
     * Domain logic: Unassigns this senior citizen from a doctor.
     * @param doctorId the ID of the doctor to unassign from
     * @param doctorOrganizationId the organization ID of the doctor (for validation)
     */
    public void unassignFromDoctor(Long doctorId, Long doctorOrganizationId) {
        // Validate same organization
        if (!belongsToOrganization(doctorOrganizationId)) {
            throw new IllegalStateException(
                String.format("Cannot unassign senior citizen from doctor: They belong to different organizations " +
                    "(Senior Citizen: org %d, Doctor: org %d)", this.getOrganizationId(), doctorOrganizationId)
            );
        }

        if (isAssignedToDoctor(doctorId)) {
            this.assignedDoctorId = null;
            this.addDomainEvent(new SeniorCitizenUnassignedFromDoctorEvent(this, doctorId, this.getId(), this.getOrganizationId()));
        }
    }

    /**
     * Domain logic: Assigns this senior citizen to a caregiver.
     * Validates that the senior citizen is not already assigned to a doctor (exclusión mutua).
     * If already assigned to another caregiver, it will be reassigned to the new caregiver.
     * @param caregiverId the ID of the caregiver to assign
     * @param caregiverOrganizationId the organization ID of the caregiver (for validation)
     */
    public void assignToCaregiver(Long caregiverId, Long caregiverOrganizationId) {
        // Validate same organization
        if (!belongsToOrganization(caregiverOrganizationId)) {
            throw new IllegalStateException(
                String.format("Cannot assign senior citizen to caregiver: They belong to different organizations " +
                    "(Senior Citizen: org %d, Caregiver: org %d)", this.getOrganizationId(), caregiverOrganizationId)
            );
        }

        // Validate exclusión mutua: cannot be assigned to caregiver if already assigned to doctor
        if (!canBeAssignedToCaregiver()) {
            throw new IllegalStateException(
                "Cannot assign senior citizen to caregiver: Senior citizen is already assigned to a doctor. " +
                "A senior citizen can only be assigned to doctors OR caregivers, not both."
            );
        }

        // If already assigned to another caregiver, unassign from previous caregiver first
        Long previousCaregiverId = this.assignedCaregiverId;
        if (previousCaregiverId != null && !previousCaregiverId.equals(caregiverId)) {
            this.assignedCaregiverId = null;
            this.addDomainEvent(new SeniorCitizenUnassignedFromCaregiverEvent(this, previousCaregiverId, this.getId(), this.getOrganizationId()));
        }

        // Assign to new caregiver
        this.assignedCaregiverId = caregiverId;
        this.assignedDoctorId = null; // Ensure exclusión mutua
        this.addDomainEvent(new SeniorCitizenAssignedToCaregiverEvent(this, this.getId(), caregiverId, this.getOrganizationId()));
    }

    /**
     * Domain logic: Unassigns this senior citizen from a caregiver.
     * @param caregiverId the ID of the caregiver to unassign from
     * @param caregiverOrganizationId the organization ID of the caregiver (for validation)
     */
    public void unassignFromCaregiver(Long caregiverId, Long caregiverOrganizationId) {
        // Validate same organization
        if (!belongsToOrganization(caregiverOrganizationId)) {
            throw new IllegalStateException(
                String.format("Cannot unassign senior citizen from caregiver: They belong to different organizations " +
                    "(Senior Citizen: org %d, Caregiver: org %d)", this.getOrganizationId(), caregiverOrganizationId)
            );
        }

        if (isAssignedToCaregiver(caregiverId)) {
            this.assignedCaregiverId = null;
            this.addDomainEvent(new SeniorCitizenUnassignedFromCaregiverEvent(this, this.getId(), caregiverId, this.getOrganizationId()));
        }
    }

    /**
     * Marks this senior citizen for deletion and publishes a deletion event.
     * Note: The actual deletion is handled by the infrastructure layer.
     */
    public void markForDeletion() {
        this.addDomainEvent(new SeniorCitizenDeletedEvent(this, this.getId(), this.getOrganizationId()));
    }
}
