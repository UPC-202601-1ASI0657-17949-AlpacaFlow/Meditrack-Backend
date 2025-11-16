package com.alpacaflow.meditrackplatform.organization.domain.model.aggregates;

import com.alpacaflow.meditrackplatform.organization.domain.model.events.DoctorCreatedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.DoctorDeletedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.DoctorUpdatedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.SeniorCitizenAssignedToDoctorEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.SeniorCitizenUnassignedFromDoctorEvent;
import com.alpacaflow.meditrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

/**
 * Doctor aggregate representing a medical doctor within an organization.
 * Acts as part of the organization bounded context.
 * This entity ensures multi-tenant data isolation through organization relationship.
 */
@Entity
@Getter
public class Doctor extends AuditableAbstractAggregateRoot<Doctor> {

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "specialty", nullable = false)
    private String specialty;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    /**
     * Default constructor for JPA
     */
    public Doctor() {
        super();
    }

    /**
     * Constructor for creating a new Doctor with all required fields
     * @param organization the organization this doctor belongs to
     * @param userId the user ID associated with this doctor (must be a User with role "doctor")
     * @param firstName the doctor's first name
     * @param lastName the doctor's last name
     * @param age the doctor's age
     * @param email the doctor's email
     * @param specialty the doctor's medical specialty
     * @param phoneNumber the doctor's phone number
     * @param imageUrl the doctor's image URL
     */
    public Doctor(Organization organization, Long userId, String firstName, String lastName, Integer age,
                  String email, String specialty, String phoneNumber, String imageUrl) {
        this();
        this.organization = organization;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.specialty = specialty;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
    }

    /**
     * Publishes a DoctorCreatedEvent. This should be called after the doctor is persisted
     * and has an ID assigned.
     */
    public void publishCreatedEvent() {
        this.addDomainEvent(new DoctorCreatedEvent(this, this.getId(), this.getOrganizationId()));
    }

    /**
     * Updates the doctor's personal information
     * @param firstName the doctor's first name
     * @param lastName the doctor's last name
     * @param age the doctor's age
     * @param email the doctor's email
     * @param phoneNumber the doctor's phone number
     * @return this doctor instance
     */
    public Doctor updatePersonalInformation(String firstName, String lastName, Integer age, 
                                           String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.addDomainEvent(new DoctorUpdatedEvent(this, this.getId(), this.getOrganizationId()));
        return this;
    }

    /**
     * Updates the doctor's specialty
     * @param specialty the new specialty
     * @return this doctor instance
     */
    public Doctor updateSpecialty(String specialty) {
        this.specialty = specialty;
        this.addDomainEvent(new DoctorUpdatedEvent(this, this.getId(), this.getOrganizationId()));
        return this;
    }

    /**
     * Updates the doctor's image URL
     * @param imageUrl the new image URL
     * @return this doctor instance
     */
    public Doctor updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.addDomainEvent(new DoctorUpdatedEvent(this, this.getId(), this.getOrganizationId()));
        return this;
    }

    /**
     * Assigns a user ID to this doctor (typically done by the backend when creating a user account)
     * @param userId the user ID to assign
     * @return this doctor instance
     */
    public Doctor assignUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Gets the full name of the doctor
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
     * Domain logic: Validates that this doctor belongs to the same organization as the given organization ID.
     * @param organizationId the organization ID to validate against
     * @return true if this doctor belongs to the given organization
     */
    public boolean belongsToOrganization(Long organizationId) {
        return this.organization != null && this.organization.getId().equals(organizationId);
    }

    /**
     * Domain logic: Validates that this doctor can be assigned to a senior citizen.
     * This method can be extended with additional business rules if needed.
     * @return true if this doctor can be assigned
     */
    public boolean canBeAssigned() {
        // Add any business rules here (e.g., doctor must be active, have valid license, etc.)
        return true;
    }

    /**
     * Domain logic: Assigns a senior citizen to this doctor.
     * Note: The actual persistence of this relationship is handled through SeniorCitizen.assignedDoctorId
     * and a junction table (Doctor_assignments) managed by the infrastructure layer. 
     * This method represents the domain logic for the assignment operation from the doctor's perspective.
     * The actual assignment should be done through SeniorCitizen.assignToDoctor() which contains
     * the full business logic including validation of exclusión mutua.
     * @param seniorCitizenId the ID of the senior citizen to assign
     * @param seniorCitizenOrganizationId the organization ID of the senior citizen (for validation)
     */
    public void assignToSenior(Long seniorCitizenId, Long seniorCitizenOrganizationId) {
        // Validate same organization
        if (!belongsToOrganization(seniorCitizenOrganizationId)) {
            throw new IllegalStateException(
                String.format("Cannot assign senior citizen to doctor: They belong to different organizations " +
                    "(Doctor: org %d, Senior Citizen: org %d)", this.getOrganizationId(), seniorCitizenOrganizationId)
            );
        }

        // Domain logic: The actual assignment is managed through SeniorCitizen.assignedDoctorId
        // and persisted via junction table. This method publishes the event from the doctor's perspective.
        // The infrastructure layer will handle the persistence of the relationship.
        this.addDomainEvent(new SeniorCitizenAssignedToDoctorEvent(this, this.getId(), seniorCitizenId, this.getOrganizationId()));
    }

    /**
     * Domain logic: Unassigns a senior citizen from this doctor.
     * Note: The actual persistence of this relationship is handled through SeniorCitizen.assignedDoctorId
     * and a junction table (Doctor_assignments) managed by the infrastructure layer. 
     * This method represents the domain logic for the unassignment operation from the doctor's perspective.
     * The actual unassignment should be done through SeniorCitizen.unassignFromDoctor().
     * @param seniorCitizenId the ID of the senior citizen to unassign
     * @param seniorCitizenOrganizationId the organization ID of the senior citizen (for validation)
     */
    public void unassignFromSenior(Long seniorCitizenId, Long seniorCitizenOrganizationId) {
        // Validate same organization
        if (!belongsToOrganization(seniorCitizenOrganizationId)) {
            throw new IllegalStateException(
                String.format("Cannot unassign senior citizen from doctor: They belong to different organizations " +
                    "(Doctor: org %d, Senior Citizen: org %d)", this.getOrganizationId(), seniorCitizenOrganizationId)
            );
        }

        // Domain logic: The actual unassignment is managed through SeniorCitizen.assignedDoctorId
        // and persisted via junction table. This method publishes the event from the doctor's perspective.
        // The infrastructure layer will handle the persistence of the relationship.
        this.addDomainEvent(new SeniorCitizenUnassignedFromDoctorEvent(this, this.getId(), seniorCitizenId, this.getOrganizationId()));
    }


    /**
     * Marks this doctor for deletion and publishes a deletion event.
     * Note: The actual deletion is handled by the infrastructure layer.
     */
    public void markForDeletion() {
        this.addDomainEvent(new DoctorDeletedEvent(this, this.getId(), this.getOrganizationId()));
    }
}
