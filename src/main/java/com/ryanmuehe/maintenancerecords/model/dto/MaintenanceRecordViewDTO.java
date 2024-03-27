package com.ryanmuehe.maintenancerecords.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter // lombok package defines all getter methods
@Setter // lombok package defines all setter methods

// data transfer object necessary to present MaintenanceRecords
public class MaintenanceRecordViewDTO {

    private Long id; // needed for actions like Edit/Delete

    private String description;

    private LocalDateTime lastUpdated;
}