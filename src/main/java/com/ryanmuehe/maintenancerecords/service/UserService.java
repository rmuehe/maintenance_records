package com.ryanmuehe.maintenancerecords.service;

import com.ryanmuehe.maintenancerecords.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

// see UserServiceImpl for descriptions
// this interface is a contract for UserServiceImpl
public interface UserService extends UserDetailsService {

    public UserDetails loadUserByUsername(String userName);
    User findUserByEmail(String email);

}