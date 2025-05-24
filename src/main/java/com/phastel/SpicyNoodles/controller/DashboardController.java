package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.service.UserService;
import com.phastel.SpicyNoodles.service.InvoiceService;
import com.phastel.SpicyNoodles.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    private final UserService userService;
    private final InvoiceService invoiceService;
    private final StorageService storageService;

    @Autowired
    public DashboardController(UserService userService, InvoiceService invoiceService, StorageService storageService) {
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.storageService = storageService;
    }

    @GetMapping
    public String dashboard(Model model) {
        logger.info("Accessing dashboard");
        try {
            model.addAttribute("totalUsers", userService.getAllUsers().size());
            model.addAttribute("totalOrder", invoiceService.getAllInvoices().size());
            model.addAttribute("totalStorageItems", storageService.getAllStorages().size());
            logger.info("Successfully loaded dashboard stats");
        } catch (Exception e) {
            logger.error("Error loading dashboard stats", e);
            // Set default values in case of error
            model.addAttribute("totalUsers", 0);
            model.addAttribute("totalOrder", 0);
            model.addAttribute("totalStorageItems", 0);
        }
        return "dashboard";
    }
} 