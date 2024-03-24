package com.ryanmuehe.maintenancerecords.service;

import com.ryanmuehe.maintenancerecords.model.dto.RegisterUserDTO;

public interface RegisterService {
    Boolean register(RegisterUserDTO registerUserDTO);
}
