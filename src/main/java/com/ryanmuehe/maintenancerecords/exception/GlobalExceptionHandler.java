package com.ryanmuehe.maintenancerecords.exception;

import jakarta.persistence.EntityNotFoundException;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public String handleEntityNotFound(EntityNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "The requested resource was not found.");
        return "redirect:/items";
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public String handleNoHandlerFoundException(NoHandlerFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "The requested page does not exist.");
        return "redirect:/items";
    }
}