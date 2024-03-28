package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.*;
import com.ryanmuehe.maintenancerecords.model.dto.UserDTO;
import com.ryanmuehe.maintenancerecords.model.repository.ItemRepository;
import com.ryanmuehe.maintenancerecords.model.repository.RoleAssignmentRepository;
import com.ryanmuehe.maintenancerecords.model.repository.RoleRepository;
import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
import com.ryanmuehe.maintenancerecords.service.ItemService;
import com.ryanmuehe.maintenancerecords.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import com.ryanmuehe.maintenancerecords.configuration.SecurityConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

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
// handles the business logic of Users
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleAssignmentRepository roleAssignmentRepository;
    private final RoleRepository roleRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
//    private final BCryptPasswordEncoder bcryptEncryption;


    @Autowired // Dependency injection constructor (@Autowired optional)
    public UserServiceImpl(
            UserRepository userRepository,
            RoleAssignmentRepository roleAssignmentRepository,
            /*BCryptPasswordEncoder bcryptEncryption*/RoleRepository roleRepository, ItemRepository itemRepository, ItemServiceImpl itemService) {
        this.userRepository = userRepository;
        this.roleAssignmentRepository = roleAssignmentRepository;
//        this.bcryptEncryption = bcryptEncryption;
        this.roleRepository = roleRepository;
        this.itemRepository = itemRepository;
        this.itemService = itemService;
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

    // returns a list of all users
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // returns a User when provided a User id
    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No User found with id: "+ userId) );
    }

    // for creating a new user through the admin add_user page
    // includes a role selection not included when registering a User

    @Transactional
    @Override
    public void addUser(UserDTO userDTO) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);

        // Assign selected roles

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()-> new EntityNotFoundException("Role Not found with id: " + userDTO.getRoleId()));

        RoleAssignment roleAssignment = new RoleAssignment();
        roleAssignment.setUser(savedUser);
        roleAssignment.setRole(role);
        roleAssignmentRepository.save(roleAssignment);
    }

    @Override
    @Transactional
    public void updateUser(UserDTO userDTO) throws Exception {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new Exception("User not found for ID " + userDTO.getId()));
        System.out.println("Updating user:" + user.toString());

        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());

        // Handle password change
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // save the user
        System.out.println("Updated user:" + user.toString());
        userRepository.save(user);

        // Assign selected roles
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()-> new EntityNotFoundException("Role Not found with id: " + userDTO.getRoleId()));

        // Handle role update
        // TODO: when adding roles beyond ADMIN and USER, consider disabling Role Updates,
        //  instead adding a separate interface to manage Role Assignments for Users

        // Since currently roles are global and binary, the update checks all RoleAssignments
        // and changes them all to either Admin or User -- this should be updated in the future.
        List<RoleAssignment> roleAssignments = roleAssignmentRepository.findByUserId(user.getId());
        for (RoleAssignment assignment : roleAssignments) {
            System.out.println("Updating RoleAssignment from " + assignment.toString());

            assignment.setUser(user);
            assignment.setRole(role);
            System.out.print("To " + assignment.toString());
            roleAssignmentRepository.save(assignment);
        }

    }

    // TODO: update Delete User method with disabling logic
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        // Find the user's items
        List<Item> userItems = itemRepository.findByOwnerId(userId);
        for (Item item : userItems) {
            // Use the deleteItem method for cascading delete
            itemService.deleteItem(item.getId());
        }

        // Finally, delete user and their role assignments
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
        System.out.println("Deleted User with ID: " + userId + " and all related items and records.");
    }



    // Deletes User
//    @Override
//    public void deleteUser(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(()-> new EntityNotFoundException("User not found with id: " + id));
//        userRepository.delete(user);
//    }

}