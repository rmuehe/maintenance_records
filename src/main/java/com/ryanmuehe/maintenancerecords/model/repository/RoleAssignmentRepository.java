package com.ryanmuehe.maintenancerecords.model.repository;

import com.ryanmuehe.maintenancerecords.model.Role;
import com.ryanmuehe.maintenancerecords.model.RoleAssignment;
import com.ryanmuehe.maintenancerecords.constant.QueryConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// @Repository is redundant for a JpaRepository
public interface RoleAssignmentRepository extends JpaRepository<RoleAssignment, Long> {
    // Inherits CRUD methods like save(), findById(), findAll(), deleteById()

    // List of every RoleAssignment of a User
//    @Query("SELECT r FROM RoleAssignment ra JOIN ra.role r WHERE ra.user.id = :userId")
    @Query(QueryConstant.FIND_ROLES_BY_USER_ID)
    List<Role> findRolesByUserId(@Param("userId") Long userId);
}

