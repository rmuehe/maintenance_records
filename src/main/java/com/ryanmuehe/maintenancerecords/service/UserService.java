package com.ryanmuehe.maintenancerecords.service;

import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

// see UserServiceImpl for descriptions
// this interface is a contract for UserServiceImpl
public interface UserService extends UserDetailsService {

    public UserDetails loadUserByUsername(String userName);
    User findUserByEmail(String email);

    List<User> findAll();

    User findById(Long userId);

    void addUser(UserDTO userDTO);

    void updateUser(UserDTO userDTO) throws Exception;

    void deleteUser(Long id);
}