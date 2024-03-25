package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.CustomUserDetails;
import com.ryanmuehe.maintenancerecords.model.Role;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.repository.RoleAssignmentRepository;
import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
import com.ryanmuehe.maintenancerecords.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the UserService interface for user management and authentication.
 *
 * Portions of this implementation are inspired by the work of Igor Adulyan, specifically in handling
 * user authentication and role management within Spring Security. The original example can be found
 * in the "Spring Security" branch of the SpringDemoExamples repository on GitHub:
 * https://github.com/igoradulian/SpringDemoExamples/blob/Spring-Security/src/main/java/com/learning/demo/service/UserServiceImpl.java
 *
 * @see <a href="https://github.com/igoradulian/SpringDemoExamples/blob/Spring-Security/src/main/java/com/learning/demo/service/UserServiceImpl.java">UserServiceImpl by Igor Adulyan</a>
 */
@Service // class marked as a Spring service component.
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleAssignmentRepository roleAssignmentRepository;

    @Autowired // Dependency injection constructor (@Autowired optional)
    public UserServiceImpl(
            UserRepository userRepository,
            RoleAssignmentRepository roleAssignmentRepository) {
        this.userRepository = userRepository;
        this.roleAssignmentRepository = roleAssignmentRepository;
    }

    // Loads the UserDetails required by Spring Security for authentication and authorization.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to load USER: " + email);
        System.out.println("Attempting to load USER with email: '" + email + "'");

        // Fetches user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("USER not found with email: " + email));

        System.out.println("About to check for roles");
        System.out.println("Fetching roles for user ID: " + user.getId());

        // Fetch roles for the user
        List<Role> roles = roleAssignmentRepository.findRolesByUserId(user.getId());
        System.out.println("Roles fetched: " + roles.stream().map(Role::getName).collect(Collectors.joining(", ")));

        // Convert roles to GrantedAuthorities and create a CustomUserDetails object
        CustomUserDetails customUserDetails = new CustomUserDetails(user, roles);
        System.out.println("Creating CustomUserDetails for: " + customUserDetails.getUsername());
        System.out.println("Authorities (Roles): " + customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));
        return customUserDetails;
    }

    // Helper method to convert roles to GrantedAuthorities
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    // returns a User object provided an existing, associated email
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }


}