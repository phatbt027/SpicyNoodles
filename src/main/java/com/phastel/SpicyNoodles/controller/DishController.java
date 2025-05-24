package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Dish;
import com.phastel.SpicyNoodles.entity.Ingredient;
import com.phastel.SpicyNoodles.entity.DishIngredient;
import com.phastel.SpicyNoodles.service.DishService;
import com.phastel.SpicyNoodles.service.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

@Controller
@RequestMapping("/dashboard/dishes")
public class DishController {
    
    private static final Logger logger = LoggerFactory.getLogger(DishController.class);
    
    private final DishService dishService;
    private final IngredientService ingredientService;

    @Autowired
    public DishController(DishService dishService, IngredientService ingredientService) {
        this.dishService = dishService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public String dishManagement(Model model) {
        logger.info("Accessing dish management page");
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        return "dish-management";
    }

    @GetMapping("/search")
    public String searchDishes(
            @RequestParam(required = false) String name,
            Model model) {
        
        logger.info("Searching dishes with filters - name: {}", name);

        List<Dish> dishes = dishService.getAllDishes();

        // Filter by name
        if (name != null && !name.isEmpty()) {
            dishes = dishes.stream()
                .filter(dish -> dish.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        }

        model.addAttribute("dishes", dishes);
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        return "dish-management";
    }

    @PostMapping
    public String createDish(
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam BigDecimal price,
            @RequestParam Boolean available,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) List<Long> ingredientIds,
            @RequestParam(required = false) List<Double> quantities,
            RedirectAttributes redirectAttributes) {
        
        logger.info("Creating new dish");
        try {
            Dish dish = new Dish();
            dish.setName(name);
            dish.setCategory(category);
            dish.setPrice(price);
            dish.setAvailable(available);
            dish.setDescription(description);
            dish.setImageUrl(imageUrl);
            
            // Process ingredients
            if (ingredientIds != null && quantities != null && ingredientIds.size() == quantities.size()) {
                Set<DishIngredient> ingredients = new HashSet<>();
                for (int i = 0; i < ingredientIds.size(); i++) {
                    Ingredient ingredient = ingredientService.getIngredientById(ingredientIds.get(i));
                    if (ingredient != null) {
                        DishIngredient dishIngredient = new DishIngredient();
                        dishIngredient.setIngredient(ingredient);
                        dishIngredient.setQuantity(quantities.get(i));
                        dishIngredient.setDish(dish);
                        ingredients.add(dishIngredient);
                    }
                }
                dish.setIngredients(ingredients);
            }
            
            dishService.createDish(dish);
            logger.info("Successfully created dish");
            redirectAttributes.addFlashAttribute("success", "Dish created successfully");
        } catch (Exception e) {
            logger.error("Error creating dish", e);
            redirectAttributes.addFlashAttribute("error", "Error creating dish: " + e.getMessage());
        }
        return "redirect:/dashboard/dishes";
    }

    @PostMapping("/{id}/delete")
    public String deleteDish(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to delete dish with id: {}", id);
        try {
            dishService.deleteDish(id);
            logger.info("Successfully deleted dish");
            redirectAttributes.addFlashAttribute("success", "Dish deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting dish", e);
            redirectAttributes.addFlashAttribute("error", "Error deleting dish: " + e.getMessage());
        }
        return "redirect:/dashboard/dishes";
    }

    @PostMapping("/{id}/update")
    public String updateDish(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam BigDecimal price,
            @RequestParam Boolean available,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) List<Long> ingredientIds,
            @RequestParam(required = false) List<Double> quantities,
            RedirectAttributes redirectAttributes) {
        
        logger.info("Attempting to update dish with id: {}", id);
        try {
            Dish dish = new Dish();
            dish.setId(id);
            dish.setName(name);
            dish.setCategory(category);
            dish.setPrice(price);
            dish.setAvailable(available);
            dish.setDescription(description);
            dish.setImageUrl(imageUrl);
            
            // Process ingredients
            if (ingredientIds != null && quantities != null && ingredientIds.size() == quantities.size()) {
                Set<DishIngredient> ingredients = new HashSet<>();
                for (int i = 0; i < ingredientIds.size(); i++) {
                    Ingredient ingredient = ingredientService.getIngredientById(ingredientIds.get(i));
                    if (ingredient != null) {
                        DishIngredient dishIngredient = new DishIngredient();
                        dishIngredient.setIngredient(ingredient);
                        dishIngredient.setQuantity(quantities.get(i));
                        dishIngredient.setDish(dish);
                        ingredients.add(dishIngredient);
                    }
                }
                dish.setIngredients(ingredients);
            }
            
            dishService.updateDish(dish);
            logger.info("Successfully updated dish");
            redirectAttributes.addFlashAttribute("success", "Dish updated successfully");
        } catch (Exception e) {
            logger.error("Error updating dish", e);
            redirectAttributes.addFlashAttribute("error", "Error updating dish: " + e.getMessage());
        }
        return "redirect:/dashboard/dishes";
    }
} 