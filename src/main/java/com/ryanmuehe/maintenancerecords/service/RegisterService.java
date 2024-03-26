package com.ryanmuehe.maintenancerecords.service;

import com.ryanmuehe.maintenancerecords.model.dto.RegisterUserDTO;

// see RegisterServiceImpl for descriptions
// this interface is a contract for RegisterServiceImpl
public interface RegisterService {
    Boolean register(RegisterUserDTO registerUserDTO);
}
