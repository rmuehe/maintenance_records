package com.ryanmuehe.maintenancerecords.configuration;

import com.ryanmuehe.maintenancerecords.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Security configuration class for Spring Security setup.
 *
 * This configuration is influenced by Igor Adulyan's implementation in his SecurityConfiguration class
 * from the "Spring Security" branch of the SpringDemoExamples repository on GitHub. Specific aspects such as
 * authentication provider setup, password encoding, and HTTP security configuration reflect the structure and
 * approach found in Adulyan's work.
 *
 * Original example available at:
 * https://github.com/igoradulian/SpringDemoExamples/blob/Spring-Security/src/main/java/com/learning/demo/security/SecurityConfiguration.java
 *
 * @see <a href="https://github.com/igoradulian/SpringDemoExamples/blob/Spring-Security/src/main/java/com/learning/demo/security/SecurityConfiguration.java">SecurityConfiguration by Igor Adulyan</a>
 */
@Configuration //  class marked as a configuration component
@EnableWebSecurity // enable Spring Security's web security
public class SecurityConfig {

    @Autowired // Injects a custom UserDetailsService for retrieving UserDetails during authentication
    private UserServiceImpl userDetailsService;

    // Provides a method for password hashing and validation.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configures a custom authentication provider using the custom UserServiceImpl and BcryptPasswordEncoder.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    // Defines security rules and configurations for web requests, login, and logout.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Specifies public access to certain URLs
                        .requestMatchers("/", "/index/**", "/register", "/login/**", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // Specifies access control for /home based on roles.
                        .requestMatchers("/home").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        // Requires authentication for any other request.
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // Specifies the login page URL and parameters
                        .loginPage("/login")
                        .loginProcessingUrl("/login") // should point to login page
                        .usernameParameter("username")
                        .passwordParameter("password")
//                        .successForwardUrl("/home") // accepts a POST after login
                        .defaultSuccessUrl("/home", true) // accepts a GET after login
                        .permitAll()
                )
                .logout(logout -> logout
                        // Configures the logout URL and success redirect.
//                        .logoutSuccessUrl("/login?logout")
                        .logoutUrl("/logout") // logout starts by triggering this path
                        .logoutSuccessUrl("/index?logout") // redirect to index with a URL param
                        .invalidateHttpSession(true) // stop the User session
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true) // remove User authentication
                        .permitAll()
                );

        return http.build();
    }
}