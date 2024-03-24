package com.ryanmuehe.maintenancerecords.controller;

import com.ryanmuehe.maintenancerecords.model.dto.RegisterUserDTO;
import com.ryanmuehe.maintenancerecords.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegisterController {

    // Inject RegisterService dependency into this controller
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }


    @GetMapping("/register") // Get the registration page
    public String getRegistration(
            @ModelAttribute("user")
            RegisterUserDTO registerUserDTO) {

        return "register";
    }


    @PostMapping("/register") // Process the registration form
    public String registerUserAccount(
            @ModelAttribute("user")
            @Valid RegisterUserDTO registerUserDTO,
            BindingResult result) {

//        if (result.hasErrors()) {
//            // If there are errors, return to the registration form.
//            return "redirect:/register?error";
//        }

        if (result.hasErrors()) {
            String errorParam = "?error=true";
            if (result.hasFieldErrors("email")) {
                errorParam += "&emailError=true";
            }
            if (result.hasFieldErrors("username")) {
                errorParam += "&usernameError=true";
            }
            if (result.hasFieldErrors("password")) {
                errorParam += "&passwordError=true";
            }
            // Redirect back to the register page with specific error parameters
            return "redirect:/register" + errorParam;
        }

        // else, attempt to register the new user through the register service
        Boolean registerSuccess = registerService.register(registerUserDTO);

        if (!registerSuccess) {
            // when user already exists, redirect with error
            return "redirect:/register?duplicateEmail=true";
        }
        // Redirect to the login page with a success URL parameter if registration is successful.
        return "redirect:/login?success";
    }
}
