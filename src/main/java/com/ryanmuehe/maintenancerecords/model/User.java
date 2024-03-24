package com.ryanmuehe.maintenancerecords.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity // DB table of the same name
@EntityListeners(AuditingEntityListener.class) // Enables automatic DateTime updates
@Getter // lombok package defines all getter methods
@Setter // lombok package defines all setter methods

// User model defines the model fields
// that JPA/Hibernate generates into a User DB table
public class User {

    @Id // specifies the primary key
    @Column(nullable = false, updatable = false) // specifies a required, final field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    @Column(nullable = false, unique = true) // required, unique field
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @CreatedDate // stores the time created
    @Column(nullable = false, updatable = false) // required final field
    // ToDO: to change to OffsetDateTime need to customize DateTimeProvider
    //    private OffsetDateTime dateCreated;
    // Update time types in Item, ItemUsage, MaintenanceRecord, RoleAssignment, User
    private LocalDateTime dateCreated;


    @LastModifiedDate // stores the last updated time
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "user") // relationship to a 'user' source in RoleAssignment
    private Set<RoleAssignment> roleAssignments = new HashSet<>(); // each user may have many roleAssignments

}

