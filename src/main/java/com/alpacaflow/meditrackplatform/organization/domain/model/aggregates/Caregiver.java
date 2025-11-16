package com.alpacaflow.meditrackplatform.organization.domain.model.aggregates;

import com.alpacaflow.meditrackplatform.organization.domain.model.events.CaregiverCreatedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.CaregiverDeletedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.CaregiverUpdatedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.SeniorCitizenAssignedToCaregiverEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.SeniorCitizenUnassignedFromCaregiverEvent;
import com.alpacaflow.meditrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

/**
 * Caregiver aggregate representing a caregiver within an organization.
 * Acts as part of the organization bounded context.
 * This entity ensures multi-tenant data isolation through organization relationship.
 */
@Entity
@Getter
public class Caregiver extends AuditableAbstractAggregateRoot<Caregiver> {

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

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    /**
     * Default constructor for JPA
     */
    public Caregiver() {
        super();
    }

    /**
     * Constructor for creating a new Caregiver with all required fields
     * @param organization the organization this caregiver belongs to
     * @param userId the user ID associated with this caregiver (must be a User with role "caregiver")
     * @param firstName the caregiver's first name
     * @param lastName the caregiver's last name
     * @param age the caregiver's age
     * @param email the caregiver's email
     * @param phoneNumber the caregiver's phone number
     * @param imageUrl the caregiver's image URL
     */
    public Caregiver(Organization organization, Long userId, String firstName, String lastName, Integer age,
                  String email, String phoneNumber, String imageUrl) {
        this();
        this.organization = organization;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
    }

    /**
     * Publishes a CaregiverCreatedEvent. This should be called after the caregiver is persisted
     * and has an ID assigned.
     */
    public void publishCreatedEvent() {
        this.addDomainEvent(new CaregiverCreatedEvent(this, this.getId(), this.getOrganizationId()));
    }

    /**
     * Updates the caregiver's personal information
     * @param firstName the caregiver's first name
     * @param lastName the caregiver's last name
     * @param age the caregiver's age
     * @param email the caregiver's email
     * @param phoneNumber the caregiver's phone number
     * @return this caregiver instance
     */
    public Caregiver updatePersonalInformation(String firstName, String lastName, Integer age, 
                                           String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.addDomainEvent(new CaregiverUpdatedEvent(this, this.getId(), this.getOrganizationId()));
        return this;
    }

    /**
     * Updates the caregiver's image URL
     * @param imageUrl the new image URL
     * @return this caregiver instance
     */
    public Caregiver updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.addDomainEvent(new CaregiverUpdatedEvent(this, this.getId(), this.getOrganizationId()));
        return this;
    }

    /**
     * Assigns a user ID to this caregiver (typically done by the backend when creating a user account)
     * @param userId the user ID to assign
     * @return this caregiver instance
     */
    public Caregiver assignUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Gets the full name of the caregiver
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
     * Domain logic: Validates that this caregiver belongs to the same organization as the given organization ID.
     * @param organizationId the organization ID to validate against
     * @return true if this caregiver belongs to the given organization
     */
    public boolean belongsToOrganization(Long organizationId) {
        return this.organization != null && this.organization.getId().equals(organizationId);
    }

    /**
     * Domain logic: Validates that this caregiver can be assigned to a senior citizen.
     * This method can be extended with additional business rules if needed.
     * @return true if this caregiver can be assigned
     */
    public boolean canBeAssigned() {
        // Add any business rules here (e.g., caregiver must be active, have valid license, etc.)
        return true;
    }

    /**
     * Domain logic: Assigns a senior citizen to this caregiver.
     * Note: The actual persistence of this relationship is handled through SeniorCitizen.assignedCaregiverId
     * and a junction table (Caregiver_assignments) managed by the infrastructure layer. 
     * This method represents the domain logic for the assignment operation from the caregiver's perspective.
     * The actual assignment should be done through SeniorCitizen.assignToCaregiver() which contains
     * the full business logic including validation of exclusión mutua.
     * @param seniorCitizenId the ID of the senior citizen to assign
     * @param seniorCitizenOrganizationId the organization ID of the senior citizen (for validation)
     */
    public void assignToSenior(Long seniorCitizenId, Long seniorCitizenOrganizationId) {
        // Validate same organization
        if (!belongsToOrganization(seniorCitizenOrganizationId)) {
            throw new IllegalStateException(
                String.format("Cannot assign senior citizen to caregiver: They belong to different organizations " +
                    "(Caregiver: org %d, Senior Citizen: org %d)", this.getOrganizationId(), seniorCitizenOrganizationId)
            );
        }

        // Domain logic: The actual assignment is managed through SeniorCitizen.assignedCaregiverId
        // and persisted via junction table. This method publishes the event from the caregiver's perspective.
        // The infrastructure layer will handle the persistence of the relationship.
        this.addDomainEvent(new SeniorCitizenAssignedToCaregiverEvent(this, this.getId(), seniorCitizenId, this.getOrganizationId()));
    }

    /**
     * Domain logic: Unassigns a senior citizen from this caregiver.
     * Note: The actual persistence of this relationship is handled through SeniorCitizen.assignedCaregiverId
     * and a junction table (Caregiver_assignments) managed by the infrastructure layer. 
     * This method represents the domain logic for the unassignment operation from the caregiver's perspective.
     * The actual unassignment should be done through SeniorCitizen.unassignFromCaregiver().
     * @param seniorCitizenId the ID of the senior citizen to unassign
     * @param seniorCitizenOrganizationId the organization ID of the senior citizen (for validation)
     */
    public void unassignFromSenior(Long seniorCitizenId, Long seniorCitizenOrganizationId) {
        // Validate same organization
        if (!belongsToOrganization(seniorCitizenOrganizationId)) {
            throw new IllegalStateException(
                String.format("Cannot unassign senior citizen from caregiver: They belong to different organizations " +
                    "(Caregiver: org %d, Senior Citizen: org %d)", this.getOrganizationId(), seniorCitizenOrganizationId)
            );
        }

        // Domain logic: The actual unassignment is managed through SeniorCitizen.assignedCaregiverId
        // and persisted via junction table. This method publishes the event from the caregiver's perspective.
        // The infrastructure layer will handle the persistence of the relationship.
        this.addDomainEvent(new SeniorCitizenUnassignedFromCaregiverEvent(this, this.getId(), seniorCitizenId, this.getOrganizationId()));
    }


    /**
     * Marks this caregiver for deletion and publishes a deletion event.
     * Note: The actual deletion is handled by the infrastructure layer.
     */
    public void markForDeletion() {
        this.addDomainEvent(new CaregiverDeletedEvent(this, this.getId(), this.getOrganizationId()));
    }
}
