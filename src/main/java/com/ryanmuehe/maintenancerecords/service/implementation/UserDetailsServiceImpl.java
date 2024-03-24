package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
import com.ryanmuehe.maintenancerecords.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailsServiceImpl (UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        // Check if user exists
//        boolean emailExists = userRepository.emailExists(email);
//        if (!emailExists) {
//            // if user does not, then throw an exception
//            throw new UsernameNotFoundException("User not found with email: " + email);
//        }
        System.out.println("Attempting to load user: " + email);
        // else, fetch the user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("User not found with email: " + email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

//        System.out.println("Found user: " + user.getUsername() + ", with roles: " + user.getRoles().toString());
        System.out.println("User found: " + user.getUsername() + " with roles: ROLE_USER");

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), Collections.emptyList());
    }
}