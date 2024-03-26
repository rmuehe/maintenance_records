package com.ryanmuehe.maintenancerecords.controller;

import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.service.ItemService;
import com.ryanmuehe.maintenancerecords.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
/**
 * listens for URLs selecting login-related actions
 * processes and directs to login-related URLs
 * */
public class LoginController {

    private final UserService userservice;
    private final ItemService itemservice;


    @Autowired
    public LoginController(UserService userservice, ItemService itemservice) {
        this.userservice = userservice;
        this.itemservice = itemservice;
    }

    @GetMapping("/login")
    // Display the login page
    public String login() {
        // Just return the view name. Thymeleaf will access request parameters directly.
        return "login";
    }

    @GetMapping("/items")
    // Display the Items page after successful log in
    public String showUserItems(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Check if userDetails is null to avoid NullPointerException
        if (userDetails != null) {
            // Print the granted authorities for testing
            String authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            System.out.println("Granted Authorities: " + authorities);
            model.addAttribute("authorities", authorities);

            // email of currently authenticated User
            String email = userDetails.getUsername();
            // find User by email
            User user = userservice.findUserByEmail(email);
            // fetch items belonging to User
            List<Item> items = itemservice.findItemsByUserId(user.getId());
            // add items to the model to access in items.html
            model.addAttribute("items", items);
        }

        return "items"; // route to items.html
    }
}