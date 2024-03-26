package com.ryanmuehe.maintenancerecords.controller;

import com.ryanmuehe.maintenancerecords.model.CustomUserDetails;
import com.ryanmuehe.maintenancerecords.model.Item;
//import com.ryanmuehe.maintenancerecords.model.RoleAssignment;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.dto.AddItemDTO;
//import com.ryanmuehe.maintenancerecords.model.dto.RegisterUserDTO;
import com.ryanmuehe.maintenancerecords.service.ItemService;
import com.ryanmuehe.maintenancerecords.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import java.constant.Set;

@Controller
/**
 * listens for Item URLs and directs to pages related to items
 * */
public class ItemController {


    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public ItemController (ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping("/items/add")
    // directs to a new Add Item page
    public String showAddItemForm(Model model) {
        // Initialize an empty item to be filled by the form
        model.addAttribute("item", new Item());
        return "add_item";
    }

    @PostMapping("/items/add")
    // processes Add Item page form to add item and returns to Items page
    public String addItem(
            @ModelAttribute("item")
            @Valid AddItemDTO addItemDTO,
            BindingResult result) {

        // authentication token has this User's user details inside
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // signal page to present error message if there's no item name provided
        if (result.hasErrors()) {
            String errorParam = "?error=true";
            if (result.hasFieldErrors("name")) {
                errorParam += "&emailError=true";
            }
            // Redirect back to the register page with specific error parameters
            return "redirect:/items/add" + errorParam;
        }

        // else, store the new item through the ItemService

        // get User from authentication token
        User user = userService.findUserByEmail(currentPrincipalName);
        Item item = new Item();
        // transfer Data Transfer Object properties to new item
        item.setName(addItemDTO.getName());
        item.setDescription(addItemDTO.getDescription());
        // sets the owner of the item based on the currently logged-in user
        item.setOwner(user);
        // persists item to database
        itemService.addItem(item);

        return "redirect:/items";
    }

    @GetMapping("/items/edit/{id}")
    // provides an Edit Item page if User is permitted
    public String showEditItemForm(
            @PathVariable("id") Long id,
            Model model,
            Authentication authentication) {

        // gets the full Item requested
        Item item = itemService.findById(id);

        // cast auth token to UserDetails object
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        //get User from UserDetails
        User currentUser = userService.findUserByEmail(userDetails.getUsername());

        // Check if current User is Owner and/or Admin
        boolean isOwner = item.getOwner().equals(currentUser);
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        // if neither Admin nor Item Owner then prohibit from editing the Item
        if (!isOwner && !isAdmin) {
            return "redirect:/items?error=unauthorized";
        }
        // else, User is permitted to edit the requested Item
        model.addAttribute("item", item);
        return "edit_item";
    }

    @PostMapping("/items/edit")
    // Updates an item through Edit Item page and redirects to Items page
    public String editItem(
            @ModelAttribute("item") @Valid Item item,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            // Handle case where item doesn't exist or error updating
            redirectAttributes.addFlashAttribute("updateError", "Error updating item");
            return "edit_item";
        }
        User user = userService.findUserByEmail(authentication.getName());
        item.setOwner(user);
        itemService.addItem(item);
        // Add success message to be displayed on the items page
        redirectAttributes.addFlashAttribute("updateSuccess", "Item updated successfully");
        return "redirect:/items";
    }

    @PostMapping("/items/delete/{id}")
    // Deletes Item requested and displays a delete message
    public String deleteItem(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        String email = authentication.getName();
        User currentUser = userService.findUserByEmail(email);
        Item item = itemService.findById(id);

        // cast auth token to userDetails object
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Check if current user is owner and/or admin
        boolean isOwner = item.getOwner().equals(currentUser);
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        // Check if the current user is the owner or an admin
        if (isOwner || isAdmin) {
            itemService.deleteItem(id);
            redirectAttributes.addFlashAttribute("deleteSuccess", "Item deleted successfully.");
        } else {
            // Handle case where the user does not have permission to delete
            redirectAttributes.addFlashAttribute("deleteError", "Delete not permitted");
        }
        return "redirect:/items";
    }
}