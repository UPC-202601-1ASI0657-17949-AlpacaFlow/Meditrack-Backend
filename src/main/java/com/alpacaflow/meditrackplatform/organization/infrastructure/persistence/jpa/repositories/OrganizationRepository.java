package com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Organization repository
 * <p>This interface is used to interact with the database and perform CRUD operations on the Organization entity.</p>
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}

