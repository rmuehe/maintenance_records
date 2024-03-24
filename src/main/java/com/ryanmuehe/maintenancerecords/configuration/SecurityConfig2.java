//package com.ryanmuehe.maintenancerecords.configuration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    // minimal encryption bean to inject throughout project
//    // increase strength to improve encryption at the cost of resources
//    @Bean
//    public BCryptPasswordEncoder bcryptEncryption() {
//        return new BCryptPasswordEncoder(4);
//    }
//
////    private final UserDetailsService userDetailsService;
//
////    @Autowired
////    public SecurityConfig (UserDetailsService userDetailsService) {
////        this.userDetailsService = userDetailsService;
////    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                    .requestMatchers("/", "/register", "/login", "/css", "/js").permitAll()
//                    .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                    .loginPage("/login")
//                    .defaultSuccessUrl("/", true) // login redirect
//                    .permitAll()
//                )
//                .logout(logout ->
//                    logout.permitAll()
//                );
//        return http.build();
//
//
//    }
//
////    @Autowired
////    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////        auth.userDetailsService(userDetailsService).passwordEncoder(bcryptEncryption());
////    }
//
//    // method-level injection to interrupt circular dependency on UserDetailsService
//    @Autowired
//    public void configureGlobal(
//            AuthenticationManagerBuilder auth,
//           UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
//        auth.
//            userDetailsService(userDetailsService)
//            .passwordEncoder(bcryptEncryption());
//    }
//}