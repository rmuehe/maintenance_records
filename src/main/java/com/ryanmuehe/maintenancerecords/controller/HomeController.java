package com.ryanmuehe.maintenancerecords.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // marks the class as a controller
public class HomeController {

    @GetMapping("/") // GET request to the root URL
    public String index() {
        return "/index"; // opens the landing page: index.html
    }

}
