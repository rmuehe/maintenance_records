package com.ryanmuehe.maintenancerecords.controller;


import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.ItemUsage;
import com.ryanmuehe.maintenancerecords.model.MaintenanceRecord;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.dto.ItemUsageDTO;
import com.ryanmuehe.maintenancerecords.model.dto.MaintenanceRecordDTO;
import com.ryanmuehe.maintenancerecords.service.*;
import com.ryanmuehe.maintenancerecords.service.implementation.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Controller
/**
 * listens for Maintenance Record URLs and directs to pages related to Maintenance Records
 * */
public class ItemUsageController {

    private final ItemService itemService;
    private final UserService userService;
    private final AuthService authService;

    private final ItemUsageService itemUsageService;

    @Autowired
    public ItemUsageController (
            ItemService itemService,
            UserService userService,
            AuthService authService,
            ItemUsageService itemUsageService) {
        this.itemService = itemService;
        this.userService = userService;
        this.authService = authService;
        this.itemUsageService = itemUsageService;
    }

    @GetMapping("/itemUsages/add")
    // TODO: make sure "Unauthorized" message appears - may be HTML issue
    public String showAddMaintenanceRecordForm(
            @RequestParam("itemId") Long itemId,
            Model model,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Item item = itemService.findById(itemId);

        // If the item doesn't exist, redirect to a generic items page with an error message
        if (item == null) {
            redirectAttributes.addFlashAttribute("error", "The specified item does not exist.");
            return "redirect:/items";
        }

//        // Fetch the current user based on the authentication
//        String userEmail = authentication.getName();
//        User currentUser = userService.findUserByEmail(userEmail);

        // Get the owner of the item
        User itemOwner = item.getOwner();

        // Check if the current user is the owner of the item or an admin
        if (!authService.isOwner(itemOwner, authentication) && !authService.isAdmin(authentication)) {
            // If not, redirect to a generic items page with an unauthorized access error message
            redirectAttributes.addFlashAttribute("error", "Unauthorized access to add Item Usage for this item");
            return "redirect:/items?error=unauthorizedAccess";
        }

        // If authorized, proceed to show the form
        ItemUsageDTO itemUsageDTO = new ItemUsageDTO();
        itemUsageDTO.setItemId(itemId); // Set itemId in DTO
        model.addAttribute("itemUsage", itemUsageDTO);
        return "add_item_usage";
    }


    // Creates a new Item Usage if the input is valid
    // and redirects to the User's items page
    @PostMapping("/itemUsages/add")
    // TODO: Add authentication check to prevent unauthorized curl or Postman POSTs
    public String addItemUsage(@ModelAttribute("itemUsage") @Valid ItemUsageDTO itemUsageDTO,
                                       BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add_item_usage";
        }
        itemUsageService.addItemUsage(itemUsageDTO);

        redirectAttributes.addFlashAttribute("message", "Item Usage added successfully.");
        return "redirect:/items/" + itemUsageDTO.getItemId();
    }


    @GetMapping("/itemUsages/edit/{id}")
    public String showEditItemUsageForm(@PathVariable("id") Long id, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        ItemUsage itemUsage = itemUsageService.findById(id);
        if (itemUsage == null) {
            redirectAttributes.addFlashAttribute("error", "Item usage not found.");
            return "redirect:/items";
        }

        // Authorization checks
        User itemOwner = itemUsage.getItem().getOwner();
        if (!authService.isOwner(itemOwner, authentication) && !authService.isAdmin(authentication)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access to edit this item usage.");
            return "redirect:/items?error=unauthorizedAccess";
        }

        ItemUsageDTO usageDTO = itemUsageService.convertToDTO(itemUsage);


        /**
         * If prefer to set null usageStart and UsageEnd to ItemUsage.createdBy and LocalDateTime.now()
         * */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (usageDTO.getUsageStart() != null) {
            model.addAttribute("formattedUsageStart", usageDTO.getUsageStart().format(formatter));
        } else {
            model.addAttribute("formattedUsageStart", itemUsage.getDateCreated().format(formatter));
        }

        if (usageDTO.getUsageEnd() != null) {
            model.addAttribute("formattedUsageEnd", usageDTO.getUsageEnd().format(formatter));
        } else {
            model.addAttribute("formattedUsageEnd", LocalDateTime.now().format(formatter));
        }

        /**
         * If prefer to set usageStart and usageEnd times back to null when updating
         * Prevents a NullPointerException
         */
//        model.addAttribute("formattedUsageStart", Optional.ofNullable(usageDTO.getUsageStart())
//                .map(start -> start.format(formatter)).orElse(""));
//        model.addAttribute("formattedUsageEnd", Optional.ofNullable(usageDTO.getUsageEnd())
//                .map(end -> end.format(formatter)).orElse(""));



//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
//        model.addAttribute("formattedUsageStart", usageDTO.getUsageStart().format(formatter));
//        model.addAttribute("formattedUsageEnd", usageDTO.getUsageEnd().format(formatter));
        model.addAttribute("itemUsage", usageDTO);
        return "edit_item_usage";
    }

//    @PostMapping("/itemUsages/update/{id}")

    @PostMapping("/itemUsages/update/{id}")
    public String updateItemUsage(
            @PathVariable Long id,
            @ModelAttribute("itemUsage") @Valid ItemUsageDTO itemUsageDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        System.out.println("Before attempting to update an Item Usage");

        if (result.hasErrors()) {
            System.out.println("The update from has errors");
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors) {
                System.out.println(error);
            }
            return "edit_item_usage";
        }

        ItemUsage existingUsage = itemUsageService.findById(id);

        // Authorization checks
        ItemUsage itemUsage = itemUsageService.findById(id);
        User itemOwner = itemUsage.getItem().getOwner();
        if (!authService.isOwner(itemOwner, authentication) && !authService.isAdmin(authentication)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access to edit Item Usage");
            return "redirect:/items?error=unauthorizedAccess";
        }


        itemUsageService.updateItemUsage(id, itemUsageDTO);
        redirectAttributes.addFlashAttribute("message", "Item usage updated successfully.");
        return "redirect:/items/" + itemUsageDTO.getItemId();
    }



    // Deletes Item Usages when permitted
    @PostMapping("/itemUsages/delete/{id}")
    public String deleteMaintenanceRecord(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        ItemUsage record = itemUsageService.findById(id);
        if (record == null) {
            redirectAttributes.addFlashAttribute("deleteError", "Item Usage not found");
            return "redirect:/items";
        }

        // Authorization check
        if (!authService.isOwner(record.getItem().getOwner(), authentication) && !authService.isAdmin(authentication)) {
            redirectAttributes.addFlashAttribute("deleteError", "Delete not permitted");
            return "redirect:/items";
        }

        itemUsageService.deleteItemUsage(id);
        redirectAttributes.addFlashAttribute("deleteSuccess", "Item Usage deleted successfully");
        return "redirect:/items/" + record.getItem().getId(); // Redirect back to the item details page
    }


}
