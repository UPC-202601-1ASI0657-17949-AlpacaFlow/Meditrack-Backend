package com.alpacaflow.meditrackplatform.organization.domain.model.aggregates;

import com.alpacaflow.meditrackplatform.organization.domain.model.events.OrganizationCreatedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.OrganizationDeletedEvent;
import com.alpacaflow.meditrackplatform.organization.domain.model.events.OrganizationUpdatedEvent;
import com.alpacaflow.meditrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

/**
 * Organization aggregate representing a clinic or residence.
 * Acts as the Aggregate Root for the organization bounded context.
 * This entity ensures multi-tenant data isolation.
 */
@Entity
@Getter
public class Organization extends AuditableAbstractAggregateRoot<Organization> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    /**
     * Default constructor for JPA
     */
    public Organization() {
        super();
    }

    /**
     * Constructor for creating a new Organization with all required fields
     * @param name the organization name
     * @param type the organization type ('clinic' or 'resident')
     */
    public Organization(String name, String type) {
        this();
        this.name = name;
        this.type = type;
    }

    /**
     * Publishes an OrganizationCreatedEvent. This should be called after the organization is persisted
     * and has an ID assigned.
     */
    public void publishCreatedEvent() {
        this.addDomainEvent(new OrganizationCreatedEvent(this, this.getId()));
    }

    /**
     * Updates the organization's information
     * @param name the organization name
     * @param type the organization type ('clinic' or 'resident')
     * @return this organization instance
     */
    public Organization updateInformation(String name, String type) {
        this.name = name;
        this.type = type;
        this.addDomainEvent(new OrganizationUpdatedEvent(this, this.getId()));
        return this;
    }

    /**
     * Domain logic: Checks if this organization is a clinic
     * @return true if the organization type is 'clinic'
     */
    public boolean isClinic() {
        return "clinic".equals(this.type);
    }

    /**
     * Domain logic: Checks if this organization is a residence
     * @return true if the organization type is 'resident'
     */
    public boolean isResidence() {
        return "resident".equals(this.type);
    }

    /**
     * Marks this organization for deletion and publishes a deletion event.
     * Note: The actual deletion is handled by the infrastructure layer.
     */
    public void markForDeletion() {
        this.addDomainEvent(new OrganizationDeletedEvent(this, this.getId()));
    }
}
