package com.ryanmuehe.maintenancerecords.controller;

import com.ryanmuehe.maintenancerecords.model.CustomUserDetails;
import com.ryanmuehe.maintenancerecords.model.Item;
//import com.ryanmuehe.maintenancerecords.model.RoleAssignment;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.dto.AddItemDTO;
//import com.ryanmuehe.maintenancerecords.model.dto.RegisterUserDTO;
import com.ryanmuehe.maintenancerecords.model.dto.MaintenanceRecordDTO;
import com.ryanmuehe.maintenancerecords.model.dto.MaintenanceRecordViewDTO;
import com.ryanmuehe.maintenancerecords.service.AuthService;
import com.ryanmuehe.maintenancerecords.service.ItemService;
import com.ryanmuehe.maintenancerecords.service.MaintenanceRecordService;
import com.ryanmuehe.maintenancerecords.service.UserService;
import com.ryanmuehe.maintenancerecords.service.implementation.AuthServiceImpl;
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

import java.util.List;

//import java.constant.Set;

@Controller
/**
 * listens for Item URLs and directs to pages related to items
 * */
public class ItemController {


    private final ItemService itemService;
    private final UserService userService;

    private final AuthService authService;
    private final MaintenanceRecordService maintenanceRecordService;

    @Autowired
    public ItemController (ItemService itemService,
                           UserService userService,
                           AuthService authService,
                           MaintenanceRecordService maintenanceRecordService) {
        this.itemService = itemService;
        this.userService = userService;
        this.authService = authService;
        this.maintenanceRecordService = maintenanceRecordService;
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


//    @Autowired
//    private AuthServiceImpl authService;

    @GetMapping("/items/{id}")
    public String getItemDetails(
            @PathVariable Long id,
            Model model,
            Authentication authentication, RedirectAttributes redirectAttributes) {

        Item item = itemService.findById(id);

        // Check if the item exists and has an owner
        if (item == null || item.getOwner() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "The requested item does not exist or is not accessible.");
            return "redirect:/items";
        }

//        if (item == null) {
//            return "redirect:/items?error=ItemNotFound";
//        }

        if (!authService.isOwner(item.getOwner(), authentication)
                && !authService.isAdmin(authentication)) {
            return "redirect:/items?error=unauthorizedAccess";
        }

        // added after MR update
        List<MaintenanceRecordViewDTO> maintenanceRecords = maintenanceRecordService.findMaintenanceRecordsForViewByItemId(id);
        model.addAttribute("item", item);
        model.addAttribute("maintenanceRecords", maintenanceRecords);


//        model.addAttribute("item", item);
        // Add itemUsages and maintenanceRecords to the model here
        return "item";
    }



//    @GetMapping("/items/{id}")
//    public String getItemDetails(@PathVariable Long id, Model model, Authentication authentication) {
//        // Fetching the item from the database
//        Item item = itemService.findById(id);
//        if (item == null) {
//            return "redirect:/items?error=ItemNotFound";
//        }
//
//        // Authentication and authorization logic
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        User currentUser = userService.findUserByEmail(userDetails.getUsername());
////        boolean isOwner = item.getOwner().equals(currentUser);
////        boolean isAdmin = userDetails.getAuthorities().stream()
////                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
//
//        // Redirecting if the user is neither the owner nor an admin
//        if (!authService.isOwner(currentUser, authentication) && !authService.isAdmin(authentication)) {
//            return "redirect:/items?error=unauthorizedAccess";
//        }
//
//        // Proceeding with adding attributes to the model if authorized
//        model.addAttribute("item", item);
//        // Assume similar logic for adding itemUsages and maintenanceRecords to the model
//        return "item";
//    }


//    @GetMapping("/items/{id}")
//    public String getItemDetails(@PathVariable Long id, Model model) {
//        Item item = itemService.findById(id);
//        if (item == null) {
//            return "redirect:/items?error=ItemNotFound";
//        }
//
////        List<ItemUsage> itemUsages = itemUsageService.findUsagesByItemId(id);
////        List<MaintenanceRecord> maintenanceRecords = maintenanceRecordService.findRecordsByItemId(id);
//
//        model.addAttribute("item", item);
////        model.addAttribute("itemUsages", itemUsages);
////        model.addAttribute("maintenanceRecords", maintenanceRecords);
//
//        return "item";
//    }
}