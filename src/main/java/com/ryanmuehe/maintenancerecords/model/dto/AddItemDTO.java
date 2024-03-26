package com.ryanmuehe.maintenancerecords.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter // lombok package defines all getter methods
@Setter // lombok package defines all setter methods

// Validations or error messages to help store a new Item
public class AddItemDTO {

    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    // Item requires an owner, which must be set elsewhere

}
