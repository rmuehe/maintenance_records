package com.ryanmuehe.maintenancerecords.model.repository;

import com.ryanmuehe.maintenancerecords.model.Role;
import com.ryanmuehe.maintenancerecords.model.RoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// @Repository is redundant for a JpaRepository
public interface RoleAssignmentRepository extends JpaRepository<RoleAssignment, Long> {
    // Inherits CRUD methods like save(), findById(), findAll(), deleteById()

    @Query("SELECT r FROM RoleAssignment ra JOIN ra.role r WHERE ra.user.id = :userId")
    List<Role> findRolesByUserId(@Param("userId") Long userId);
}

