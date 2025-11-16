package com.alpacaflow.meditrackplatform.organization.domain.model.aggregates;

import com.alpacaflow.meditrackplatform.organization.domain.model.events.AdminCreatedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.AdminDeletedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.AdminUpdatedEvent;
import com.alpacaflow.meditrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

/**
 * Admin aggregate representing an administrator within an organization.
 * Acts as part of the organization bounded context.
 * This entity ensures multi-tenant data isolation through organization relationship.
 */
@Entity
@Getter
public class Admin extends AuditableAbstractAggregateRoot<Admin> {

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * Default constructor for JPA
     */
    public Admin() {
        super();
    }

    /**
     * Constructor for creating a new Admin with all required fields
     * @param organization the organization this admin belongs to
     * @param userId the user ID associated with this admin
     * @param firstName the admin's first name
     * @param lastName the admin's last name
     */
    public Admin(Organization organization, Long userId, String firstName, String lastName) {
        this();
        this.organization = organization;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Publishes an AdminCreatedEvent. This should be called after the admin is persisted
     * and has an ID assigned.
     */
    public void publishCreatedEvent() {
        this.addDomainEvent(new AdminCreatedEvent(this, this.getId(), this.getOrganizationId()));
    }

    /**
     * Updates the admin's personal information
     * @param firstName the admin's first name
     * @param lastName the admin's last name
     * @return this admin instance
     */
    public Admin updatePersonalInformation(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.addDomainEvent(new AdminUpdatedEvent(this, this.getId(), this.getOrganizationId()));
        return this;
    }

    /**
     * Assigns a user ID to this admin (typically done by the backend when creating a user account)
     * @param userId the user ID to assign
     * @return this admin instance
     */
    public Admin assignUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Gets the full name of the admin
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
     * Domain logic: Validates that this admin belongs to the same organization as the given organization ID.
     * @param organizationId the organization ID to validate against
     * @return true if this admin belongs to the given organization
     */
    public boolean belongsToOrganization(Long organizationId) {
        return this.organization != null && this.organization.getId().equals(organizationId);
    }

    /**
     * Marks this admin for deletion and publishes a deletion event.
     * Note: The actual deletion is handled by the infrastructure layer.
     */
    public void markForDeletion() {
        this.addDomainEvent(new AdminDeletedEvent(this, this.getId(), this.getOrganizationId()));
    }
}
