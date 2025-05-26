package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Invoice;
import com.phastel.SpicyNoodles.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/dashboard/orders")
public class OrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    private final InvoiceService invoiceService;

    @Autowired
    public OrderController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String orderManagement(Model model) {
        logger.info("Accessing order management page");
        model.addAttribute("orders", invoiceService.getAllInvoices());
        return "order-management";
    }

    @GetMapping("/search")
    public String searchOrders(
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String status,
            Model model) {
        
        logger.info("Searching orders with filters - orderId: {}, customer: {}, date: {}, status: {}", 
            orderId, customer, date, status);

        List<Invoice> orders;
        if (orderId != null && !orderId.isEmpty()) {
            try {
                Long id = Long.parseLong(orderId);
                orders = List.of(invoiceService.getInvoiceById(id));
            } catch (NumberFormatException e) {
                orders = List.of();
            }
        } else {
            orders = invoiceService.getAllInvoices();
        }

        // Filter by customer
        if (customer != null && !customer.isEmpty()) {
            orders = orders.stream()
                .filter(order -> order.getUser().getUsername().toLowerCase().contains(customer.toLowerCase()))
                .toList();
        }

        // Filter by date
        if (date != null && !date.isEmpty()) {
            LocalDate searchDate = LocalDate.parse(date);
            LocalDateTime startOfDay = searchDate.atStartOfDay();
            LocalDateTime endOfDay = searchDate.atTime(LocalTime.MAX);
            
            orders = orders.stream()
                .filter(order -> !order.getOrderTime().isBefore(startOfDay) && !order.getOrderTime().isAfter(endOfDay))
                .toList();
        }

        // Filter by status
        if (status != null && !status.isEmpty()) {
            orders = orders.stream()
                .filter(order -> order.getStatus().name().equals(status))
                .toList();
        }

        model.addAttribute("orders", orders);
        return "order-management";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        logger.info("Viewing order with ID: {}", id);
        try {
            Invoice order = invoiceService.getInvoiceById(id);
            model.addAttribute("order", order);
            model.addAttribute("orderItems", order.getItems());
            logger.info("Successfully loaded order details for ID: {}", id);
        } catch (Exception e) {
            logger.error("Error loading order details for ID: {}", id, e);
        }
        return "order-details";
    }

    @PostMapping("/{id}/update")
    public String updateOrder(@PathVariable Long id, @ModelAttribute Invoice order) {
        logger.info("Updating order with ID: {}", id);
        try {
            order.setId(id);
            invoiceService.updateInvoice(order);
            logger.info("Successfully updated order with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error updating order with ID: {}", id, e);
        }
        return "redirect:/dashboard/orders";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        logger.info("Deleting order with ID: {}", id);
        try {
            invoiceService.deleteInvoice(id);
            logger.info("Successfully deleted order with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting order with ID: {}", id, e);
        }
        return "redirect:/dashboard/orders";
    }
} 