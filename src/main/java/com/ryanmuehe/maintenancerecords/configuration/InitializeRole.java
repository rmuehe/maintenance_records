package com.ryanmuehe.maintenancerecords.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ryanmuehe.maintenancerecords.model.Role;
import com.ryanmuehe.maintenancerecords.model.repository.RoleRepository;

@Component // Marks class for component scanning
/**
* Every time the application starts, Roles are persisted to database if not present
 * since some pages require authorization through Roles
 * and Roles are not created or changed through the website
* */
public class InitializeRole implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository; // Injects RoleRepository

    @Override
    public void run(String... args) {
        // Ensure roles are present in database at application start
        createRoleIfNotFound("ROLE_USER");
        createRoleIfNotFound("ROLE_ADMIN");
    }

    private void createRoleIfNotFound(String roleName) {
        // Checks and creates role if missing
        roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName); // Set role name
            roleRepository.save(role); // Save new role
            return role; // Return created role
        });
    }
}
