package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Invoice;
import com.phastel.SpicyNoodles.service.InvoiceService;
import com.phastel.SpicyNoodles.service.UserService;
import com.phastel.SpicyNoodles.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    private final InvoiceService invoiceService;

    @Autowired
    public DashboardController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String dashboard(Model model) {
        logger.info("Accessing dashboard page");
        try {
            // Calculate current month's income
            double currentMonthIncome = getCurrentMonthIncome();
            
            model.addAttribute("currentMonthIncome", currentMonthIncome);
            model.addAttribute("totalOrder", invoiceService.getAllInvoices().size());
            model.addAttribute("monthlyOrders", getMonthlyOrdersData());
            model.addAttribute("monthlyIncome", getMonthlyIncomeData());
            logger.info("Successfully loaded dashboard stats");
        } catch (Exception e) {
            logger.error("Error loading dashboard stats", e);
            // Set default values in case of error
            model.addAttribute("currentMonthIncome", 0.0);
            model.addAttribute("totalOrder", 0);
            model.addAttribute("monthlyOrders", List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            model.addAttribute("monthlyIncome", List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        }
        return "dashboard";
    }

    private double getCurrentMonthIncome() {
        LocalDateTime now = LocalDateTime.now();
        YearMonth currentYearMonth = YearMonth.from(now);
        
        return invoiceService.getAllInvoices().stream()
            .filter(invoice -> {
                LocalDateTime orderTime = invoice.getOrderTime();
                return orderTime.getYear() == currentYearMonth.getYear() 
                    && orderTime.getMonthValue() == currentYearMonth.getMonthValue();
            })
            .mapToDouble(Invoice::getTotalPrice)
            .sum();
    }

    private List<Integer> getMonthlyOrdersData() {
        List<Invoice> allInvoices = invoiceService.getAllInvoices();
        LocalDateTime now = LocalDateTime.now();
        YearMonth currentYearMonth = YearMonth.from(now);

        // Create a map of month to order count for the current year
        Map<Integer, Long> monthlyCounts = allInvoices.stream()
            .filter(invoice -> {
                LocalDateTime orderTime = invoice.getOrderTime();
                return orderTime.getYear() == currentYearMonth.getYear();
            })
            .collect(Collectors.groupingBy(
                invoice -> invoice.getOrderTime().getMonthValue(),
                Collectors.counting()
            ));

        // Create a list of 12 integers representing orders per month
        List<Integer> monthlyOrders = new java.util.ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            monthlyOrders.add(monthlyCounts.getOrDefault(month, 0L).intValue());
        }

        return monthlyOrders;
    }

    private List<Double> getMonthlyIncomeData() {
        List<Invoice> allInvoices = invoiceService.getAllInvoices();
        LocalDateTime now = LocalDateTime.now();
        YearMonth currentYearMonth = YearMonth.from(now);

        // Create a map of month to total income for the current year
        Map<Integer, Double> monthlyIncome = allInvoices.stream()
            .filter(invoice -> {
                LocalDateTime orderTime = invoice.getOrderTime();
                return orderTime.getYear() == currentYearMonth.getYear();
            })
            .collect(Collectors.groupingBy(
                invoice -> invoice.getOrderTime().getMonthValue(),
                Collectors.summingDouble(Invoice::getTotalPrice)
            ));

        // Create a list of 12 doubles representing income per month
        List<Double> monthlyIncomeList = new java.util.ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            monthlyIncomeList.add(monthlyIncome.getOrDefault(month, 0.0));
        }

        return monthlyIncomeList;
    }
} 