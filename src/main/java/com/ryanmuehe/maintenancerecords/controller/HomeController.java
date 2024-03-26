package com.ryanmuehe.maintenancerecords.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // marks the class as a controller
/**
 * listens for home page related routes
 * */
public class HomeController {

    @GetMapping("/") // GET request to the root URL
    public String index() {

        return "index"; // opens the landing page: index.html
    }

    @GetMapping("/index") // GET request to root after logging out
    public String index(
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been successfully logged out.");
        }
        return "index"; // Return the index view (template)
    }

}
