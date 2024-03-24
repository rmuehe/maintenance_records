package com.ryanmuehe.maintenancerecords.model.repository;

import com.ryanmuehe.maintenancerecords.model.RoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository is redundant for a JpaRepository
public interface RoleAssignmentRepository extends JpaRepository<RoleAssignment, Long> {
    // Inherits CRUD methods like save(), findById(), findAll(), deleteById()
}

