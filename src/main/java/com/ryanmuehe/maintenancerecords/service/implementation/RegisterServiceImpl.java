package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.service.RegisterService;



import com.ryanmuehe.maintenancerecords.model.Role;
import com.ryanmuehe.maintenancerecords.model.RoleAssignment;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.dto.RegisterUserDTO;
import com.ryanmuehe.maintenancerecords.model.repository.RoleAssignmentRepository;
import com.ryanmuehe.maintenancerecords.model.repository.RoleRepository;
import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;




    @Service
    // handles business logic of Registration
    public class RegisterServiceImpl implements RegisterService {
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final RoleAssignmentRepository roleAssignmentRepository;
        private final BCryptPasswordEncoder bcryptEncryption;

        // Injected repositories and encryption bean from SecurityConfig
        public RegisterServiceImpl (
                UserRepository userRepository,
                RoleRepository roleRepository,
                RoleAssignmentRepository roleAssignmentRepository,
                BCryptPasswordEncoder bcryptEncryption) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleAssignmentRepository = roleAssignmentRepository;
        this.bcryptEncryption = bcryptEncryption;
    }

    // This method handles the user registration process, including password encryption using bcrypt.
    @Override
    public Boolean register(final RegisterUserDTO registerUserDTO) {
        // before saving a user, check database if user email already exists
        if (userRepository.emailExists(registerUserDTO.getEmail())) {
            return false;
        }

        final User user = new User();
        user.setEmail(registerUserDTO.getEmail());
        user.setUsername(registerUserDTO.getUsername());
        // Encrypts the password using bcrypt before storing it.
        user.setPassword(bcryptEncryption.encode(registerUserDTO.getPassword()));

        User savedUser = userRepository.save(user);

        // Assigns the default ROLE_USER to the newly registered user.
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));
        RoleAssignment roleAssignment = new RoleAssignment();
        roleAssignment.setUser(savedUser);
        roleAssignment.setRole(userRole);
        roleAssignmentRepository.save(roleAssignment);

        return true;
    }
}
