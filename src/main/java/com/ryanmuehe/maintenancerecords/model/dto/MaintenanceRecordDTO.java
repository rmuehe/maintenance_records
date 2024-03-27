package com.ryanmuehe.maintenancerecords.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter // lombok package defines all getter methods
@Setter // lombok package defines all setter methods

// Validations or error messages to help store a new Maintenance Record
public class MaintenanceRecordDTO {

    private Long id; // needed for updating but not for saving a new Maintenance Record

    @NotBlank(message = "Maintenance Record description is required")
    private String description;

    @NotNull(message = "Maintenance Record itemId is required")
    private Long itemId;


    // Maintenance Record requires an item, which must be set elsewhere
    // Currently being set through  MaintenanceRecordService.addMaintenanceRecord();

}