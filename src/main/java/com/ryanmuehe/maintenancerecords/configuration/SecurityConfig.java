package com.ryanmuehe.maintenancerecords.configuration;

import com.ryanmuehe.maintenancerecords.model.repository.UserRepository;
import com.ryanmuehe.maintenancerecords.service.implementation.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder bcryptEncryption() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public UserDetailsService userDetailsService (UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login", "/css/**", "/js/**","/images/**" ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email") // changes username default to email
                        .successForwardUrl("/home") // required for thymeleaf security to work
                        // .defaultSuccessUrl("/", true) // login redirect
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // logout redirect

                        .permitAll()
                );
        return http.build();
    }
//    @Autowired
//    public void configureGlobal(
//            AuthenticationManagerBuilder auth,
//            UserDetailsService userDetailsService,
//            BCryptPasswordEncoder bcryptEncryption) throws Exception {
//        auth.
//                userDetailsService(userDetailsService)
//                .passwordEncoder(bcryptEncryption);
//    }
}