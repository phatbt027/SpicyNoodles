package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.User;
import com.phastel.SpicyNoodles.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/dashboard/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userManagement(Model model) {
        logger.info("Accessing user management page");
        model.addAttribute("users", userService.getAllUsers());
        return "user-management";
    }

    @GetMapping("/search")
    public String searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            Model model) {
        
        logger.info("Searching users with filters - username: {}, email: {}, role: {}", 
            username, email, role);

        List<User> users = userService.getAllUsers();

        // Filter by username
        if (username != null && !username.isEmpty()) {
            users = users.stream()
                .filter(user -> user.getUsername().toLowerCase().contains(username.toLowerCase()))
                .toList();
        }

        // Filter by email
        if (email != null && !email.isEmpty()) {
            users = users.stream()
                .filter(user -> user.getEmail().toLowerCase().contains(email.toLowerCase()))
                .toList();
        }

        // Filter by role
        if (role != null && !role.isEmpty()) {
            users = users.stream()
                .filter(user -> user.getRole().name().equals(role))
                .toList();
        }

        model.addAttribute("users", users);
        return "user-management";
    }

    @GetMapping("/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        logger.info("Viewing user with ID: {}", id);
        try {
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            logger.info("Successfully loaded user details for ID: {}", id);
        } catch (Exception e) {
            logger.error("Error loading user details for ID: {}", id, e);
        }
        return "user-details";
    }

    @PostMapping
    public String createUser(@ModelAttribute User user, @RequestParam(required = false) String enabled) {
        logger.info("Creating new user: {}", user.getUsername());
        try {
            user.setEnabled(enabled != null);
            userService.createUser(user);
            logger.info("Successfully created user: {}", user.getUsername());
        } catch (Exception e) {
            logger.error("Error creating user: {}", user.getUsername(), e);
        }
        return "redirect:/dashboard/users";
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user, @RequestParam(required = false) String enabled) {
        logger.info("Updating user with ID: {}", id);
        try {
            user.setId(id);
            user.setEnabled(enabled != null);
            userService.updateUser(user);
            logger.info("Successfully updated user with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error updating user with ID: {}", id, e);
        }
        return "redirect:/dashboard/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        try {
            userService.deleteUser(id);
            logger.info("Successfully deleted user with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting user with ID: {}", id, e);
        }
        return "redirect:/dashboard/users";
    }
} 