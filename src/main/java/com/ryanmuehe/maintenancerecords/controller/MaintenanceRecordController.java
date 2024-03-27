package com.ryanmuehe.maintenancerecords.controller;


import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.MaintenanceRecord;
import com.ryanmuehe.maintenancerecords.model.User;
import com.ryanmuehe.maintenancerecords.model.dto.MaintenanceRecordDTO;
import com.ryanmuehe.maintenancerecords.service.AuthService;
import com.ryanmuehe.maintenancerecords.service.ItemService;
import com.ryanmuehe.maintenancerecords.service.MaintenanceRecordService;
import com.ryanmuehe.maintenancerecords.service.UserService;
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

import java.util.List;
import java.util.Objects;

@Controller
/**
 * listens for Maintenance Record URLs and directs to pages related to Maintenance Records
 * */
public class MaintenanceRecordController {

    private final ItemService itemService;
    private final MaintenanceRecordService maintenanceRecordService;
    private final UserService userService;
    private final AuthService authService;


    @Autowired
    public MaintenanceRecordController (
            ItemService itemService,
            MaintenanceRecordService maintenanceRecordService,
            UserService userService,
            AuthService authService) {
        this.itemService = itemService;
        this.maintenanceRecordService = maintenanceRecordService;
        this.userService = userService;
        this.authService = authService;
    }

//    @GetMapping("/maintenanceRecords/add")
//    public String showAddMaintenanceRecordForm(
//            @RequestParam("itemId") Long itemId,
//            Model model) {
//        // Only Admin should be able to see all items
////        List<Item> items = itemService.findItemsByUserId(...);
////        model.addAttribute("items", items);
//        MaintenanceRecordDTO maintenanceRecordDTO = new MaintenanceRecordDTO();
//        maintenanceRecordDTO.setItemId(itemId);
//
//        model.addAttribute("maintenanceRecord", maintenanceRecordDTO);
//        return "add_maintenance_record";
//    }

    @GetMapping("/maintenanceRecords/add")
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

        // Fetch the current user based on the authentication
        String userEmail = authentication.getName();
        User currentUser = userService.findUserByEmail(userEmail);

        // Get the owner of the item
        User itemOwner = item.getOwner();

        // Check if the current user is the owner of the item or an admin
        if (!authService.isOwner(itemOwner, authentication) && !authService.isAdmin(authentication)) {
            // If not, redirect to a generic items page with an unauthorized access error message
            redirectAttributes.addFlashAttribute("error", "Unauthorized access to add maintenance record for this item.");
            return "redirect:/items?error=unauthorizedAccess";
        }

        // If authorized, proceed to show the form
        MaintenanceRecordDTO maintenanceRecordDTO = new MaintenanceRecordDTO();
        maintenanceRecordDTO.setItemId(itemId); // Set itemId in DTO
        model.addAttribute("maintenanceRecord", maintenanceRecordDTO);
        return "add_maintenance_record";
    }


    // Creates a new Maintenance Record if the input is valid
    // and redirects to the User's items page
    @PostMapping("/maintenanceRecords/add")
    // TODO: Add authentication check to prevent unauthorized curl or Postman POSTs
    public String addMaintenanceRecord(@ModelAttribute("maintenanceRecord") @Valid MaintenanceRecordDTO maintenanceRecordDTO,
                                       BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add_maintenance_record";
        }

//        Item item = itemService.findById(maintenanceRecordDTO.getItemId());
//        if (item == null) {
//            redirectAttributes.addFlashAttribute("message", "Item not found.");
//            return "redirect:/items";
//        }

        // Create and save the new maintenance record with the fetched item and the description from DTO
//        MaintenanceRecord newRecord = new MaintenanceRecord();
//        newRecord.setItem(item); // Set the item
//        newRecord.setDescription(maintenanceRecordDTO.getDescription()); // Set description from DTO
//        maintenanceRecordService.addMaintenanceRecord(newRecord); // Adjust based on your service layer

        maintenanceRecordService.addMaintenanceRecord(maintenanceRecordDTO);

        redirectAttributes.addFlashAttribute("message", "Maintenance record added successfully.");
//        return "redirect:/items/" + item.getId();
        return "redirect:/items/" + maintenanceRecordDTO.getItemId();
    }

    // listens for call to Maintenance Record update page and provides, if permitted
    @GetMapping("/maintenanceRecords/update/{id}")
    // TODO: make the Unauthorized messages work - may require update to HTML
    public String showUpdateMaintenanceRecordForm(@PathVariable("id") Long id, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordService.findById(id);
        if (maintenanceRecord == null) {
            redirectAttributes.addFlashAttribute("error", "Maintenance record not found.");
            return "redirect:/items";
        }

        User itemOwner = maintenanceRecord.getItem().getOwner();
        if (!authService.isOwner(itemOwner, authentication) && !authService.isAdmin(authentication)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access to update this maintenance record.");
            return "redirect:/items?error=unauthorizedAccess";
        }

        MaintenanceRecordDTO mrDTO = maintenanceRecordService.convertToMRecordDTO(maintenanceRecord);
//        model.addAttribute("maintenanceRecord", maintenanceRecord); // Maybe should convert to DTO
        model.addAttribute("maintenanceRecord", mrDTO); // Maybe should convert to DTO
        return "update_maintenance_record";
    }

    @PostMapping("/maintenanceRecords/update/{id}")
    public String updateMaintenanceRecord(
            @PathVariable Long id,
            @ModelAttribute("maintenanceRecord") @Valid MaintenanceRecordDTO maintenanceRecordDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        System.out.println("Starting update operation"); // Debugging start
        System.out.println("DTO received: " + maintenanceRecordDTO); // Print DTO received

        if (result.hasErrors()) {
            // If validation errors, redirect back to the update form
//            return "update_maintenance_record";

            System.out.println("Form has errors."); // Check if there are form errors
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors) {
                System.out.println(error.getDefaultMessage()); // Print each error message
            }
            return "redirect:/maintenanceRecords/update/" + id;
        }
            System.out.println("Fetching existing record with ID: " + id); // Print the ID being used to fetch
            MaintenanceRecord existingRecord = maintenanceRecordService.findById(id);
            if (existingRecord == null) {
                System.out.println("Record not found with ID: " + id); // Record not found
                redirectAttributes.addFlashAttribute("error", "Maintenance Record not found.");
                return "redirect:/items";
            }
            // check if the URL item number is the same as the DTO item id
            else if (!Objects.equals(existingRecord.getItem().getId(), maintenanceRecordDTO.getItemId())) {
                redirectAttributes.addFlashAttribute("error", "Maintenance Record not found.");
                return "redirect:/items";
            }

            System.out.println("Record found. Checking authorization."); // Proceed to authorization check
            // Authorization check
            if (!authService.isOwner(existingRecord.getItem().getOwner(), authentication) && !authService.isAdmin(authentication)) {
                System.out.println("User unauthorized to update record."); // Unauthorized access
                redirectAttributes.addFlashAttribute("error", "Unauthorized to update this maintenance record.");
                return "redirect:/items?error=unauthorizedAccess";
            }

            // Update the Maintenance Record
//        existingRecord.setDescription(maintenanceRecordDTO.getDescription());

            System.out.println("User authorized. Updating record."); // Authorized to update
            // Update logic here
            // The item association, etc. should remain unchanged
            maintenanceRecordService.addMaintenanceRecord(maintenanceRecordDTO);

            redirectAttributes.addFlashAttribute("success", "Maintenance record updated successfully.");
//        return "redirect:/items/" + existingRecord.getItem().getId(); // Redirect to the item detail page

            System.out.println("Record updated. Redirecting."); // Confirming redirection
            return "redirect:/items/" + maintenanceRecordDTO.getItemId();

    }

    // Deletes Maintenance Records when permitted
    @PostMapping("/maintenanceRecords/delete/{id}")
    public String deleteMaintenanceRecord(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        MaintenanceRecord record = maintenanceRecordService.findById(id);
        if (record == null) {
            redirectAttributes.addFlashAttribute("deleteError", "Maintenance record not found.");
            return "redirect:/items";
        }

        // Authorization check
        if (!authService.isOwner(record.getItem().getOwner(), authentication) && !authService.isAdmin(authentication)) {
            redirectAttributes.addFlashAttribute("deleteError", "Delete not permitted");
            return "redirect:/items";
        }

        maintenanceRecordService.deleteMaintenanceRecord(id);
        redirectAttributes.addFlashAttribute("deleteSuccess", "Maintenance record deleted successfully.");
        return "redirect:/items/" + record.getItem().getId(); // Redirect back to the item details page
    }




//    @PostMapping("/maintenanceRecords/add")
//    public String addMaintenanceRecord(
//            @ModelAttribute("maintenanceRecord") @Valid MaintenanceRecordDTO maintenanceRecordDTO,
//            BindingResult result,
//            RedirectAttributes redirectAttributes) {
//
//        if (result.hasErrors()) {
//            return "add_maintenance_record";
//        }
//
//
//        // Assuming you have a service method to save a new MaintenanceRecord. Adjust based on your actual implementation.
//        maintenanceRecordService.addMaintenanceRecord(maintenanceRecordDTO, item);
//        redirectAttributes.addFlashAttribute("message", "Maintenance record added successfully.");
//        return "redirect:/items/" + item.getId(); // or wherever you want to redirect
//    }
}
