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

// User model defines the model fields
// that JPA/Hibernate generates into a User DB table
public class MaintenanceRecord {

    @Id // specifies the primary key
    @Column(nullable = false, updatable = false) // specifies a required, final field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    @Column(columnDefinition = "longtext", nullable = false) // allows for longblob size in DB
    private String description; // record of the state of the item

    // Many Maintenance Records can be associated with one item
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false) // associated by item id
    private Item item; // the item that gets the maintenance record

    @CreatedDate // stores the time created
    @Column(nullable = false, updatable = false) // required final field
    private LocalDateTime dateCreated;

    @LastModifiedDate // stores the last updated time
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
}
