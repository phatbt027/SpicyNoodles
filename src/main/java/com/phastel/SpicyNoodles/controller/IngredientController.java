package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Ingredient;
import com.phastel.SpicyNoodles.entity.IngredientCategory;
import com.phastel.SpicyNoodles.service.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dashboard/ingredients")
public class IngredientController {
    
    private static final Logger logger = LoggerFactory.getLogger(IngredientController.class);
    
    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public String ingredientManagement(Model model) {
        logger.info("Accessing ingredient management page");
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        return "ingredient-management";
    }

    @PostMapping
    public String createIngredient(@ModelAttribute Ingredient ingredient, @RequestParam String category) {
        logger.info("Creating new ingredient: {}", ingredient.getName());
        try {
            ingredient.setCategory(IngredientCategory.valueOf(category));
            ingredientService.createIngredient(ingredient);
            logger.info("Successfully created ingredient: {}", ingredient.getName());
        } catch (Exception e) {
            logger.error("Error creating ingredient: {}", ingredient.getName(), e);
        }
        return "redirect:/dashboard/ingredients";
    }

    @PostMapping("/{id}/update")
    public String updateIngredient(@PathVariable Long id, @ModelAttribute Ingredient ingredient, @RequestParam String category) {
        logger.info("Updating ingredient with id: {}", id);
        try {
            ingredient.setId(id);
            ingredient.setCategory(IngredientCategory.valueOf(category));
            ingredientService.updateIngredient(ingredient);
            logger.info("Successfully updated ingredient: {}", ingredient.getName());
        } catch (Exception e) {
            logger.error("Error updating ingredient: {}", ingredient.getName(), e);
        }
        return "redirect:/dashboard/ingredients";
    }

    @PostMapping("/{id}/delete")
    public String deleteIngredient(@PathVariable Long id) {
        logger.info("Deleting ingredient with id: {}", id);
        try {
            ingredientService.deleteIngredient(id);
            logger.info("Successfully deleted ingredient with id: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting ingredient with id: {}", id, e);
        }
        return "redirect:/dashboard/ingredients";
    }

    @GetMapping("/search")
    public String searchIngredients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            Model model) {
        
        logger.info("Searching ingredients with filters - name: {}, category: {}", name, category);

        if (name != null && !name.isEmpty()) {
            model.addAttribute("ingredients", ingredientService.searchIngredientsByName(name));
        } else if (category != null && !category.isEmpty()) {
            model.addAttribute("ingredients", ingredientService.getIngredientsByCategory(category));
        } else {
            model.addAttribute("ingredients", ingredientService.getAllIngredients());
        }

        return "ingredient-management";
    }
} 