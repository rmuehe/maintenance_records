package com.ryanmuehe.maintenancerecords.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // Just return the view name. Thymeleaf will access request parameters directly.
        return "login";
    }


    @GetMapping("/home")
    public String home() {
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