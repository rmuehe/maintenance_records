package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.configuration.TestJpaAuditingConfig;
import com.ryanmuehe.maintenancerecords.model.Role;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.dto.RegisterUserDTO;
import com.ryanmuehe.maintenancerecords.model.repository.RoleRepository;
import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
import com.ryanmuehe.maintenancerecords.service.implementation.RegisterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({RegisterServiceImpl.class, BCryptPasswordEncoder.class, TestJpaAuditingConfig.class})
public class RegisterServiceImplTest {

    @Autowired
    private RegisterServiceImpl registerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setup() {
        // need one role to exist first
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        roleRepository.save(roleUser);
    }

    @Test
    @DisplayName("Successful registration returns true")
    void register_Successful_ReturnsTrue() {
        // new data transfer object
        RegisterUserDTO newUser = new RegisterUserDTO();
        newUser.setEmail("mail@mail.com");
        newUser.setUsername("MAIL");
        newUser.setPassword("password");

        // registers user and returns true if saved
        boolean result = registerService.register(newUser);

        // user should exist (with an email)
        assertTrue(result);
        // emailExist should return true -- there's a user with that email already
        assertTrue(userRepository.emailExists("mail@mail.com"));
    }

    @Test
    @DisplayName("Registration with existing email returns false")
    void register_ExistingEmail_ReturnsFalse() {
        // Register a user
        RegisterUserDTO newUser = new RegisterUserDTO();
        newUser.setEmail("existing@existing.com");
        newUser.setUsername("EXISTING");
        newUser.setPassword("password");
        registerService.register(newUser);

        // Attempt to register another user with the same email
        RegisterUserDTO sameEmailUser = new RegisterUserDTO();
        sameEmailUser.setEmail("existing@existing.com");
        sameEmailUser.setUsername("DIFFERENT");
        sameEmailUser.setPassword("difference");

        // should not save, so register will return false
        boolean result = registerService.register(sameEmailUser);
        // result will be false if emailExists() and new User will not save
        assertFalse(result);
    }
}
