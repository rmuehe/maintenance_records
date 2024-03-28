package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.CustomUserDetails;
import com.ryanmuehe.maintenancerecords.model.Role;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.service.AuthService;
import com.ryanmuehe.maintenancerecords.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
class AuthServiceImplTest {

    @Mock
    private UserService userService; // Mock the UserService to avoid direct database access during the test

    private AuthService authService; // The AuthService instance to be tested

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks before each test
        authService = new AuthServiceImpl(userService); // Inject the mocked UserService into AuthService
    }

    @Test
    @DisplayName("Test isOwner() method")
    // Makes sure that Authentication credentials shows a User that is the owner of some Item
    void testIsOwner() {
        // Setup
        User owner = new User();
        owner.setEmail("owner@example.com"); // Create a User object to sim the owner

        Set<Role> roles = new HashSet<>();
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER"); // Create a ROLE_USER to add to the CustomUserDetails
        roles.add(roleUser);

        // Create CustomUserDetails using the owner and the roles set to sim the logged-in user
        CustomUserDetails customUserDetails = new CustomUserDetails(owner, roles);

        // Sim authentication with CustomUserDetails, without setting a password and with ROLE_USER authority
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Set the Authentication in the SecurityContext to sim an authenticated session
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When the UserService is asked for the user with email "owner@example.com", return the owner object
        // This simulates the UserService finding the owner by their email
        when(userService.findUserByEmail("owner@example.com")).thenReturn(owner);

        // Action & Assert
        // Assert that the authService.isOwner() method correctly identifies the authenticated user (owner)
        // as the owner of the User object being checked
        assertTrue(authService.isOwner(owner, SecurityContextHolder.getContext().getAuthentication()));
    }
}
