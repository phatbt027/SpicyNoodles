package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.*;
import com.phastel.SpicyNoodles.service.*;
import com.phastel.SpicyNoodles.dto.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/staff/orders")
@PreAuthorize("hasRole('STAFF')")
public class StaffOrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(StaffOrderController.class);
    
    private final InvoiceService invoiceService;
    private final DishService dishService;
    private final SoftDrinkService softDrinkService;
    private final StorageService storageService;
    private final UserService userService;

    @Autowired
    public StaffOrderController(InvoiceService invoiceService, DishService dishService, 
                              SoftDrinkService softDrinkService, StorageService storageService,
                              UserService userService) {
        this.invoiceService = invoiceService;
        this.dishService = dishService;
        this.softDrinkService = softDrinkService;
        this.storageService = storageService;
        this.userService = userService;
    }

    @GetMapping
    public String staffOrderManagement(Model model) {
        logger.info("Accessing staff order management page");
        
        // Get all orders and group them by status
        List<Invoice> allOrders = invoiceService.getAllInvoices();
        
        // Filter orders by status
        List<Invoice> pendingOrders = allOrders.stream()
            .filter(order -> order.getStatus() == Invoice.OrderStatus.PENDING)
            .collect(Collectors.toList());
            
        List<Invoice> preparingOrders = allOrders.stream()
            .filter(order -> order.getStatus() == Invoice.OrderStatus.PREPARING)
            .collect(Collectors.toList());
            
        List<Invoice> readyOrders = allOrders.stream()
            .filter(order -> order.getStatus() == Invoice.OrderStatus.READY)
            .collect(Collectors.toList());
        
        // Add orders to model
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("preparingOrders", preparingOrders);
        model.addAttribute("readyOrders", readyOrders);
        
        // Add counts to model
        model.addAttribute("pendingCount", pendingOrders.size());
        model.addAttribute("preparingCount", preparingOrders.size());
        model.addAttribute("readyCount", readyOrders.size());

        // Add dishes and soft drinks for the add order form
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("softDrinks", softDrinkService.getAllSoftDrinks());
        
        return "staff-order-management";
    }

    @PostMapping("/{id}/update-status")
    @ResponseBody
    public String updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        logger.info("Updating order status for order ID: {} to status: {}", id, request.get("status"));
        
        try {
            Invoice order = invoiceService.getInvoiceById(id);
            if (order != null) {
                order.setStatus(Invoice.OrderStatus.valueOf(request.get("status")));
                invoiceService.updateInvoice(order);
                logger.info("Successfully updated order status for order ID: {}", id);
                return "Success";
            }
        } catch (Exception e) {
            logger.error("Error updating order status for order ID: {}", id, e);
        }
        
        return "Error";
    }

    @PostMapping("/add")
    public String addOrder(@ModelAttribute OrderRequest orderRequest, RedirectAttributes redirectAttributes) {
        logger.info("Creating new order for customer: {}", orderRequest.getCustomerName());
        
        try {
            // Get current user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userService.getUserByUsername(auth.getName());
            
            // Check ingredient availability
            Map<Long, Integer> requiredIngredients = new HashMap<>();
            
            // Calculate required ingredients for dishes
            for (OrderRequest.OrderItemRequest item : orderRequest.getItems()) {
                if (item.getDishId() != null) {
                    Dish dish = dishService.getDishById(item.getDishId());
                    for (DishIngredient dishIngredient : dish.getIngredients()) {
                        Long ingredientId = dishIngredient.getIngredient().getId();
                        int requiredQuantity = (int) (dishIngredient.getQuantity() * item.getQuantity());
                        requiredIngredients.merge(ingredientId, requiredQuantity, Integer::sum);
                    }
                }
                if (item.getSoftDrinkId() != null) {
                    SoftDrink softDrink = softDrinkService.getSoftDrinkById(item.getSoftDrinkId());
                    for (SoftDrinkIngredient drinkIngredient : softDrink.getIngredients()) {
                        Long ingredientId = drinkIngredient.getIngredient().getId();
                        int requiredQuantity = (int) (drinkIngredient.getQuantity() * item.getQuantity());
                        requiredIngredients.merge(ingredientId, requiredQuantity, Integer::sum);
                    }
                }
            }
            
            // Check if all ingredients are available
            List<String> insufficientIngredients = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : requiredIngredients.entrySet()) {
                Long ingredientId = entry.getKey();
                int requiredQuantity = entry.getValue();
                
                // Get storage for the ingredient (assuming branch ID 1 for now)
                Storage storage = storageService.getStorageById(ingredientId, 1L);
                if (storage.getQuantity() < requiredQuantity) {
                    insufficientIngredients.add(String.format("%s (Required: %d, Available: %d)",
                        storage.getIngredient().getName(), requiredQuantity, storage.getQuantity()));
                }
            }
            
            if (!insufficientIngredients.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Insufficient ingredients: " + 
                    String.join(", ", insufficientIngredients));
                return "redirect:/staff/orders";
            }
            
            // Create the order
            final Invoice order = new Invoice();
            order.setCustomerName(orderRequest.getCustomerName());
            order.setStatus(Invoice.OrderStatus.PENDING);
            order.setOrderTime(LocalDateTime.now());
            order.setNotes(orderRequest.getNotes());
            order.setUser(currentUser);

            // Create order items with invoice reference
            List<OrderItem> orderItems = orderRequest.getItems().stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setInvoice(order);  // Set invoice reference first
                    if (item.getDishId() != null) {
                        orderItem.setDish(dishService.getDishById(item.getDishId()));
                    }
                    if (item.getSoftDrinkId() != null) {
                        orderItem.setSoftDrink(softDrinkService.getSoftDrinkById(item.getSoftDrinkId()));
                    }
                    orderItem.setQuantity(item.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList());

            // Calculate total price
            double totalPrice = orderItems.stream()
                .mapToDouble(item -> {
                    if (item.getDish() != null) {
                        return item.getDish().getPrice().doubleValue() * item.getQuantity();
                    } else if (item.getSoftDrink() != null) {
                        return item.getSoftDrink().getPrice().doubleValue() * item.getQuantity();
                    }
                    return 0.0;
                })
                .sum();
            
            order.setTotalPrice(totalPrice);
            order.setItems(orderItems);

            // Save the order
            invoiceService.createInvoice(order);
            
            // Update storage quantities
            for (Map.Entry<Long, Integer> entry : requiredIngredients.entrySet()) {
                Long ingredientId = entry.getKey();
                int requiredQuantity = entry.getValue();
                Storage storage = storageService.getStorageById(ingredientId, 1L);
                storage.setQuantity(storage.getQuantity() - requiredQuantity);
                storageService.updateStorage(storage);
            }
            
            logger.info("Successfully created order for customer: {}", orderRequest.getCustomerName());
            redirectAttributes.addFlashAttribute("success", "Order created successfully");
            return "redirect:/staff/orders";
        } catch (Exception e) {
            logger.error("Error creating order for customer: {}", orderRequest.getCustomerName(), e);
            redirectAttributes.addFlashAttribute("error", "Error creating order: " + e.getMessage());
            return "redirect:/staff/orders";
        }
    }
} 