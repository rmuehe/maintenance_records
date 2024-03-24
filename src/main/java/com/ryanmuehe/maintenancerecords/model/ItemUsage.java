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
public class ItemUsage {

    @Id // specifies the primary key
    @Column(nullable = false, updatable = false) // specifies a required, final field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    @Column
    private String usedBy; // name of person or organization using the item

    @Column
    private LocalDateTime usageStart; // when the usage begins

    @Column
    private LocalDateTime usageEnd; // when the usage ends

    // Many ItemUsages can be associated with one item
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false) // associated by item id
    private Item item; // the item being used

    @CreatedDate // stores the time created
    @Column(nullable = false, updatable = false) // required final field
    private LocalDateTime dateCreated;

    @LastModifiedDate // stores the last updated time
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
}
