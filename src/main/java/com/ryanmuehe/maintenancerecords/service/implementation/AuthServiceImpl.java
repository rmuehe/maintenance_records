package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.CustomUserDetails;
import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.service.AuthService;
import com.ryanmuehe.maintenancerecords.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
// provides methods to check the authentication or authorization status of current User
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Autowired
    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    // checks if the current User is an Admin
    @Override
    public boolean isAdmin(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    // checks if the current User is the owner of the current Item
    @Override
    public boolean isOwner(User owner, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userService.findUserByEmail(userDetails.getUsername());
        return owner.equals(currentUser);
    }
}