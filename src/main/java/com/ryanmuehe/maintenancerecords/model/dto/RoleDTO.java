package com.ryanmuehe.maintenancerecords.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO {

    private String name;

    public RoleDTO(String name) {
        this.name = name;
    }
}