package com.ryanmuehe.maintenancerecords.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter // lombok package defines all getter methods
@Setter // lombok package defines all setter methods

// Validations or error messages to help register a new User
public class RegisterUserDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Username is required")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}