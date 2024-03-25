package com.ryanmuehe.maintenancerecords.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * Custom UserDetails implementation for Spring Security.
 *
 * This class is inspired by Igor Adulyan's UserPrincipal class from his demonstration
 * on Spring Security in the "Spring Security" branch of the SpringDemoExamples repository on GitHub.
 * It adapts the concept of mapping user roles to Spring Security's GrantedAuthority for authentication and authorization.
 *
 * Original example available at:
 * https://github.com/igoradulian/SpringDemoExamples/blob/Spring-Security/src/main/java/com/learning/demo/security/UserPrincipal.java
 *
 * @see <a href="https://github.com/igoradulian/SpringDemoExamples/blob/Spring-Security/src/main/java/com/learning/demo/security/UserPrincipal.java">UserPrincipal by Igor Adulyan</a>
 */
public class CustomUserDetails implements UserDetails {
    // Implements Spring Security's UserDetails interface.

    private final String email; // User email that uniquely IDs any stored User
    private final String password; // stored, encrypted User password
    private final Collection<? extends GrantedAuthority> authorities; // set of User Roles converted to Authorities

    public CustomUserDetails(User user, Collection<Role> roles) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = translateRolesToAuthorities(roles); // converts Spring Roles to Spring Security GrantedAuthorities
    }

    // Utility method to convert Role entities into Spring Security GrantedAuthorities
    private Collection<? extends GrantedAuthority> translateRolesToAuthorities(Collection<Role> roles) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : roles) {
            System.out.println("Translating role to authority: " + role.getName());
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return grantedAuthorities;
    }

    // UserDetails methods to retrieve UserDetails (User + GrantedAuthorities) properties
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    //  UserDetails about activity statuses of an account
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}