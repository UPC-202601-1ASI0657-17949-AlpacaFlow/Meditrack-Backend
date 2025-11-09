package com.alpacaflow.meditrackplatform.organization.application.internal.queryservices;

import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Admin;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllAdminsByOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAllAdminsQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAdminByIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.model.queries.GetAdminByUserIdAndOrganizationIdQuery;
import com.alpacaflow.meditrackplatform.organization.domain.services.AdminQueryService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the AdminQueryService interface.
 */
@Service
public class AdminQueryServiceImpl implements AdminQueryService {
    private final AdminRepository adminRepository;

    /**
     * Constructor.
     *
     * @param adminRepository the admin repository
     * @see AdminRepository
     */
    public AdminQueryServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // inherited javadoc
    @Override
    public Optional<Admin> handle(GetAdminByIdQuery query) {
        return adminRepository.findById(query.adminId());
    }

    // inherited javadoc
    @Override
    public List<Admin> handle(GetAllAdminsQuery query) {
        return adminRepository.findAll();
    }

    // inherited javadoc
    @Override
    public List<Admin> handle(GetAllAdminsByOrganizationIdQuery query) {
        return adminRepository.findByOrganization_Id(query.organizationId());
    }

    // inherited javadoc
    @Override
    public Optional<Admin> handle(GetAdminByUserIdAndOrganizationIdQuery query) {
        return adminRepository.findByUserIdAndOrganization_Id(query.userId(), query.organizationId());
    }
}

