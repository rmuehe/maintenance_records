package com.ryanmuehe.maintenancerecords.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;


@Entity // DB table of the same name
@EntityListeners(AuditingEntityListener.class) // Enables automatic DateTime updates
@Getter // lombok package defines all getter methods
@Setter // lombok package defines all setter methods

// RoleAssignment model defines the model fields
// that JPA/Hibernate generates into a RoleAssignment DB table
public class RoleAssignment {

    @Id // specifies the primary key
    @Column(nullable = false, updatable = false) // specifies a required, final field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    // Many RoleAssignments can be associated with one user
    @ManyToOne(fetch = FetchType.LAZY) // does not retrieve associated records automatically
    @JoinColumn(name = "user_id", nullable = false) // must have an associated user id
    private User user; // the user assigned to the role

    // Many RoleAssignments can be associated with one role
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false) // must have an associated role id
    private Role role; // the role assigned to the user

    // Many RoleAssignments can be associated with one item
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") // associated item id may be null for global roles
    private Item item; // the item assigned to the role and user

    @Column
    private LocalDateTime startTime; // when the RoleAssignment begins

    @Column
    private LocalDateTime endTime; // when the RoleAssignment ends

    @CreatedDate // stores the time created
    @Column(nullable = false, updatable = false) // required final field
    private LocalDateTime dateCreated;

    @LastModifiedDate // stores the last updated time
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

}


