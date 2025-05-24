package com.phastel.SpicyNoodles.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, Model model) {
	    if (error != null) {
	        logger.error("Login failed: Invalid username or password");
	        model.addAttribute("error", "Invalid username or password");
	    }
		return "login";
	}
}
