package com.ryanmuehe.maintenancerecords.controller;

import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.ItemUsage;
import com.ryanmuehe.maintenancerecords.model.Role;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.dto.UserDTO;
import com.ryanmuehe.maintenancerecords.model.repository.RoleRepository;
import com.ryanmuehe.maintenancerecords.service.ItemService;
import com.ryanmuehe.maintenancerecords.service.UserService;
import com.ryanmuehe.maintenancerecords.service.implementation.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final ItemService itemService;
    private final RoleRepository roleRepository;
    private final AuthServiceImpl authService;

    @Autowired
    public AdminController(UserService userService, ItemService itemService, RoleRepository roleRepository, AuthServiceImpl authService) {
        this.userService = userService;
        this.itemService = itemService;
        this.roleRepository = roleRepository;
        this.authService = authService;
    }

    // retrieves a page listing all users if logged in as an Admin
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userService.findAll(); // Fetch all users
        model.addAttribute("users", users); // Add users to the model
        return "users";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/items/user/{userId}")
    // retrieves a User's Items page if logged in as an Admin
    public String showItemsForUser(@PathVariable Long userId, Model model) {
        // Fetch user by ID
        User user = userService.findById(userId);
        if (user != null) {
            // Fetch items belonging to the user
            List<Item> items = itemService.findItemsByUserId(user.getId());
            model.addAttribute("items", items);
        } else {
            // Handle case where user does not exist
            model.addAttribute("errorMessage", "User not found.");
        }
        return "items";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/add")
    // gets the add User page for Admins
    public String showAddUserForm(Model model) {
        // List of all Roles
        List<Role> roles = roleRepository.findAll();
        // setup form to access an empty DTO and all roles
        model.addAttribute("roles", roles);
        model.addAttribute("user", new UserDTO());
        return "add_user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users/add")
    // submits the add page form for Admins to create new Users
    public String addUser(
               @ModelAttribute("user") @Valid UserDTO userDTO,
               BindingResult result,
               RedirectAttributes redirectAttributes,
               Model model) {

        if (result.hasErrors()) {
            System.out.println("Errors processing add User form");
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error: errors) {
                System.out.println(error);
            }
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);
            return "add_user";
        }

        try {
            userService.addUser(userDTO);

            System.out.println("User added successfully by Admin");

            redirectAttributes.addFlashAttribute("updateSuccess", "User added successfully.");
        } catch (Exception e) {

            System.out.println("User not added by Admin");
            System.out.println(userDTO.toString());
            System.out.println(e.getMessage());

            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users/add";
        }


        // Save the new User to the database
//        userService.addUser(userDTO);
//        redirectAttributes.addFlashAttribute("message", "User added successfully.");
        return "redirect:/users";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(
            @PathVariable("id") Long id,
            Authentication authentication,
            Model model) {

        System.out.println("Admin: Starting to edit user with ID " + id);

        User user = new User();
        // Fetch user and roles
        try  {
            user = userService.findById(id);
        } catch (Exception e) {
            System.out.println(e);
        }

        // Send roles to Mode.attributes for form to display
        List<Role> roles = roleRepository.findAll();

        if (user == null) {
            System.out.println("Admin: User not found for ID " + id);
            // redirect attributes to show error
            return "redirect:/users?error=UserNotFound";
        }

        // TODO: update to account for more roles than ADMIN and USER
        Role role;
        boolean isAdmin = authService.isAdmin(authentication);
        if (isAdmin) {
            role = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(RuntimeException::new);
        } else {
            role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(RuntimeException::new);
        }
        Long roleId = role.getId();

        // Prepare DTO to fill the form
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setRoleId(roleId);

        System.out.println("Admin: Editing User - " + user.getUsername());

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("roles", roles);
        return "edit_user"; // name of edit user form html
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users/edit/{id}")
    public String updateUser(
            @ModelAttribute("userDTO") @Valid UserDTO userDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        System.out.println("Admin: Attempting to update user with ID " + userDTO.getId());

        // Validate form data
        if (result.hasErrors()) {
            System.out.println("Admin: Errors found during user update.");
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
//            return "edit_user"; // Return to the edit form if errors exist
            redirectAttributes.addFlashAttribute("updateError", "Error updating user form");
            return "redirect:/users/edit/" + userDTO.getId(); // Redirect back to the edit form in case of error

        }

        // Update user details and roles
        try {
            userService.updateUser(userDTO);
            redirectAttributes.addFlashAttribute("updateSuccess", "User updated successfully.");
        } catch (Exception e) {
            System.out.println("Admin: Error updating user - " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error updating user.");
            return "redirect:/users/edit/" + userDTO.getId(); // Redirect back to the edit form in case of error
        }

//        redirectAttributes.addFlashAttribute("updateSuccess", "User updated successfully");
        return "redirect:/users"; // Redirect to the users list after successful update
    }


    // Deletes User when permitted
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users/delete/{id}")
    public String deleteMaintenanceRecord(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        User user = userService.findById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("deleteError", "User not found");
            return "redirect:/users";
        }

        // Authorization check
        if (!authService.isAdmin(authentication)) {
            redirectAttributes.addFlashAttribute("deleteError", "Delete not permitted");
            return "redirect:/items";
        }

        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("deleteSuccess", "Item Usage deleted successfully");
        return "redirect:/users/"; // Redirect back to the users page
    }

}
