package com.alpacaflow.meditrackplatform.organization.application.internal.commandservices;

import com.alpacaflow.meditrackplatform.iam.domain.services.UserCommandService;
import com.alpacaflow.meditrackplatform.organization.domain.exceptions.AdminNotFoundException;
import com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.Admin;
import com.alpacaflow.meditrackplatform.organization.domain.model.commands.*;
import com.alpacaflow.meditrackplatform.organization.domain.services.AdminCommandService;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.AdminRepository;
import com.alpacaflow.meditrackplatform.organization.infrastructure.persistence.jpa.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the AdminCommandService interface.
 * <p>This class is responsible for handling the commands related to the Admin aggregate. It requires an AdminRepository and OrganizationRepository.</p>
 * @see AdminCommandService
 * @see AdminRepository
 */
@Service
public class AdminCommandServiceImpl implements AdminCommandService {
    private final AdminRepository adminRepository;
    private final OrganizationRepository organizationRepository;
    private final UserCommandService userCommandService;

    /**
     * Constructor of the class.
     * @param adminRepository the repository to be used by the class.
     * @param organizationRepository the organization repository to be used by the class.
     * @param userCommandService the user command service to validate user existence and role.
     */
    public AdminCommandServiceImpl(AdminRepository adminRepository, OrganizationRepository organizationRepository, UserCommandService userCommandService) {
        this.adminRepository = adminRepository;
        this.organizationRepository = organizationRepository;
        this.userCommandService = userCommandService;
    }

    // inherit javadoc
    @Override
    public Long handle(CreateAdminCommand command) {
        var organization = organizationRepository.findById(command.organizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization with id %d not found".formatted(command.organizationId())));
        
        // Validate that the user exists and has role "admin"
        var user = userCommandService.getUserById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with id %d not found".formatted(command.userId())));
        
        if (user.getRole() == null || !user.getRole().equalsIgnoreCase("admin")) {
            throw new IllegalArgumentException("User with id %d does not have role 'admin'. Current role: %s".formatted(command.userId(), user.getRole()));
        }
        
        var admin = new Admin(
                organization,
                command.userId(),
                command.firstName(),
                command.lastName()
        );
        
        try {
            var savedAdmin = adminRepository.save(admin);
            savedAdmin.publishCreatedEvent();
            adminRepository.save(savedAdmin);
            return savedAdmin.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving admin: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    public Optional<Admin> handle(UpdateAdminCommand command) {
        var result = adminRepository.findById(command.adminId());
        if (result.isEmpty()) {
            throw new AdminNotFoundException(command.adminId());
        }
        
        var adminToUpdate = result.get();
        try {
            var updatedAdmin = adminToUpdate.updatePersonalInformation(
                    command.firstName(),
                    command.lastName()
            );
            
            var savedAdmin = adminRepository.save(updatedAdmin);
            return Optional.of(savedAdmin);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating admin: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    public void handle(DeleteAdminCommand command) {
        if (!adminRepository.existsById(command.adminId())) {
            throw new AdminNotFoundException(command.adminId());
        }
        
        try {
            var admin = adminRepository.findById(command.adminId()).orElseThrow();
            admin.markForDeletion();
            adminRepository.save(admin);
            adminRepository.deleteById(command.adminId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting admin: %s".formatted(e.getMessage()));
        }
    }
}

