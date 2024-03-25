package com.ryanmuehe.maintenancerecords.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Collectors;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // Just return the view name. Thymeleaf will access request parameters directly.
        return "login";
    }


//    @GetMapping("/home")
//    public String home() {
//        return "home";
//    }

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Check if userDetails is null to avoid NullPointerException
        if (userDetails != null) {
            // Print the granted authorities for testing
            String authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            System.out.println("Granted Authorities: " + authorities);
            model.addAttribute("authorities", authorities);
        }
        return "home";
    }



//    @GetMapping("/login")
//    public String login(@RequestParam(value = "success", required = false) String success, Model model) {
//        // if success URL parameter is present, report success on the screen
//        if ("true".equals(success)) {
//            model.addAttribute("successMessage", "Registration successful. Please log in.");
//        }
//        return "login";
//    }
}