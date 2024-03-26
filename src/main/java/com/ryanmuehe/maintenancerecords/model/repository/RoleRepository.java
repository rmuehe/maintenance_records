package com.ryanmuehe.maintenancerecords.model.repository;

import com.ryanmuehe.maintenancerecords.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// @Repository is redundant for a JpaRepository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Inherits CRUD methods like save(), findById(), findAll(), deleteById()

    /**
     *  given the Spring findBy convention
     * an automatically constructed query to find a role by its name
     *
     * @param name The name of the role.
     * @return An Optional containing the role if found; empty otherwise.
     * Optionals are useful to get around NullPointerExceptions when lookup is empty
     */
    Optional<Role> findByName(String name);

}

