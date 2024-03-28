package com.ryanmuehe.maintenancerecords.model.dto;

import com.ryanmuehe.maintenancerecords.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter // lombok package defines all getter methods
@Setter // lombok package defines all setter methods

// Validations or error messages to help store a new User
public class UserDTO {

    private Long id; // needed for updating but not for saving a new User

    @Email // valid email
    @NotBlank(message = "email is required")
    private String email; // email is required for authentication

    @Size(max = 50, message = "max letter count is 50") // keep the username under 51 characters long
    @NotBlank(message = "username is required")
    private String username;

    @Size(min = 8, message = "must have 8 or more characters") // make the password longer than 7 characters
    @NotBlank(message = "password is required")
    private String password;


    // A set of Role Assignments is what is required on the database,
    // but since this DTO is a temporary container
    // and in this phase of development roles are global, for the sake of simplicity,
    // the Role is held as a field and then later saved through a service as a RoleAssignment
    @NotNull(message = "Role is required")
    private Long roleId;

    /*   @OneToMany(mappedBy = "user") // relationship to a 'user' source in RoleAssignment
     * *  private Set<RoleAssignment> roleAssignments = new HashSet<>(); // each user may have many roleAssignments
     * * */

}

