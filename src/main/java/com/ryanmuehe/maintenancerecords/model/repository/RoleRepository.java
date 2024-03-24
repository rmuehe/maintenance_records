package com.ryanmuehe.maintenancerecords.model.repository;

import com.ryanmuehe.maintenancerecords.model.dto.RoleDTO;
import com.ryanmuehe.maintenancerecords.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// @Repository is redundant for a JpaRepository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Inherits CRUD methods like save(), findById(), findAll(), deleteById()

    Optional<Role> findByName(String name);

//    @Query("SELECT new com.ryanmuehe.maintenancerecords.model.dto.RoleDTO(r.name) FROM Role r WHERE r.name = :name")
//    Optional<RoleDTO> findByNameAsDTO(@Param("name") String name);
}

