package com.ryanmuehe.maintenancerecords.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity // DB table of the same name
@Getter // lombok package defines all getter methods
@Setter // lombok package defines all setter methods

// Role model defines the model fields
// that JPA/Hibernate generates into a Role DB table
public class Role {

    @Id // specifies the primary key
    @Column(nullable = false, updatable = false) // specifies a required, final field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;


    @Column (nullable = false, updatable = false)
    private String name; // role type such as ROLE_USER, ROLE_ADMIN


    @OneToMany(mappedBy = "role") // relationship to a 'role' source in RoleAssignment
    private Set<RoleAssignment> roleAssignments = new HashSet<>(); // Each role may have many roleAssignments
}
