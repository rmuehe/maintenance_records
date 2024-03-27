package com.ryanmuehe.maintenancerecords.service;

import com.ryanmuehe.maintenancerecords.model.User;
import org.springframework.security.core.Authentication;


// see AuthServiceImpl for descriptions
// this interface is a contract for AuthServiceImpl
public interface AuthService {
    boolean isAdmin(Authentication authentication);

    boolean isOwner(User owner, Authentication authentication);
}
