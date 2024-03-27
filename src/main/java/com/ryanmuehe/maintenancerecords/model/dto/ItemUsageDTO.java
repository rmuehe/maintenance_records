package com.ryanmuehe.maintenancerecords.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter // lombok package defines all getter methods
@Setter // lombok package defines all setter methods

// Validations or error messages to help store a new Maintenance Record
public class ItemUsageDTO {

    private Long id; // needed for updating but not for saving a new ItemUsage

    @NotBlank(message = "Item Usage description is required")
    private String usedBy;

    private LocalDateTime usageStart;

    private LocalDateTime usageEnd;

    @NotNull(message = "Item Usage itemId is required")
    private Long itemId;


    // Item Usage requires an item, which must be set elsewhere
    // Currently being set through ItemUsageService.addItemUsage();

}