package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.SoftDrink;
import com.phastel.SpicyNoodles.entity.SoftDrinkIngredient;
import com.phastel.SpicyNoodles.entity.Ingredient;
import com.phastel.SpicyNoodles.service.SoftDrinkService;
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
@RequestMapping("/dashboard/soft-drinks")
public class SoftDrinkController {
    
    private static final Logger logger = LoggerFactory.getLogger(SoftDrinkController.class);
    
    private final SoftDrinkService softDrinkService;
    private final IngredientService ingredientService;

    @Autowired
    public SoftDrinkController(SoftDrinkService softDrinkService, IngredientService ingredientService) {
        this.softDrinkService = softDrinkService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public String softDrinkManagement(Model model) {
        logger.info("Accessing soft drink management page");
        model.addAttribute("softDrinks", softDrinkService.getAllSoftDrinks());
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        return "soft-drink-management";
    }

    @GetMapping("/search")
    public String searchSoftDrinks(
            @RequestParam(required = false) String name,
            Model model) {
        
        logger.info("Searching soft drinks with filters - name: {}", name);

        List<SoftDrink> softDrinks = softDrinkService.getAllSoftDrinks();

        // Filter by name
        if (name != null && !name.isEmpty()) {
            softDrinks = softDrinks.stream()
                .filter(drink -> drink.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        }

        model.addAttribute("softDrinks", softDrinks);
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        return "soft-drink-management";
    }

    @PostMapping
    public String createSoftDrink(
            @RequestParam String name,
            @RequestParam String size,
            @RequestParam BigDecimal price,
            @RequestParam Boolean isHot,
            @RequestParam Boolean available,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) List<Long> ingredientIds,
            @RequestParam(required = false) List<Double> quantities,
            RedirectAttributes redirectAttributes) {
        
        logger.info("Creating new soft drink");
        try {
            SoftDrink softDrink = new SoftDrink();
            softDrink.setName(name);
            softDrink.setSize(size);
            softDrink.setPrice(price);
            softDrink.setHot(isHot);
            softDrink.setAvailable(available);
            softDrink.setDescription(description);
            softDrink.setImageUrl(imageUrl);
            
            // Process ingredients
            if (ingredientIds != null && quantities != null && ingredientIds.size() == quantities.size()) {
                Set<SoftDrinkIngredient> ingredients = new HashSet<>();
                for (int i = 0; i < ingredientIds.size(); i++) {
                    Ingredient ingredient = ingredientService.getIngredientById(ingredientIds.get(i));
                    if (ingredient != null) {
                        SoftDrinkIngredient softDrinkIngredient = new SoftDrinkIngredient();
                        softDrinkIngredient.setIngredient(ingredient);
                        softDrinkIngredient.setQuantity(quantities.get(i));
                        softDrinkIngredient.setSoftDrink(softDrink);
                        ingredients.add(softDrinkIngredient);
                    }
                }
                softDrink.setIngredients(ingredients);
            }
            
            softDrinkService.createSoftDrink(softDrink);
            logger.info("Successfully created soft drink");
            redirectAttributes.addFlashAttribute("success", "Soft drink created successfully");
        } catch (Exception e) {
            logger.error("Error creating soft drink", e);
            redirectAttributes.addFlashAttribute("error", "Error creating soft drink: " + e.getMessage());
        }
        return "redirect:/dashboard/soft-drinks";
    }

    @PostMapping("/{id}/delete")
    public String deleteSoftDrink(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to delete soft drink with id: {}", id);
        try {
            softDrinkService.deleteSoftDrink(id);
            logger.info("Successfully deleted soft drink");
            redirectAttributes.addFlashAttribute("success", "Soft drink deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting soft drink", e);
            redirectAttributes.addFlashAttribute("error", "Error deleting soft drink: " + e.getMessage());
        }
        return "redirect:/dashboard/soft-drinks";
    }

    @PostMapping("/{id}/update")
    public String updateSoftDrink(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String size,
            @RequestParam BigDecimal price,
            @RequestParam Boolean isHot,
            @RequestParam Boolean available,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) List<Long> ingredientIds,
            @RequestParam(required = false) List<Double> quantities,
            RedirectAttributes redirectAttributes) {
        
        logger.info("Attempting to update soft drink with id: {}", id);
        try {
            SoftDrink softDrink = new SoftDrink();
            softDrink.setId(id);
            softDrink.setName(name);
            softDrink.setSize(size);
            softDrink.setPrice(price);
            softDrink.setHot(isHot);
            softDrink.setAvailable(available);
            softDrink.setDescription(description);
            softDrink.setImageUrl(imageUrl);
            
            // Process ingredients
            if (ingredientIds != null && quantities != null && ingredientIds.size() == quantities.size()) {
                Set<SoftDrinkIngredient> ingredients = new HashSet<>();
                for (int i = 0; i < ingredientIds.size(); i++) {
                    Ingredient ingredient = ingredientService.getIngredientById(ingredientIds.get(i));
                    if (ingredient != null) {
                        SoftDrinkIngredient softDrinkIngredient = new SoftDrinkIngredient();
                        softDrinkIngredient.setIngredient(ingredient);
                        softDrinkIngredient.setQuantity(quantities.get(i));
                        softDrinkIngredient.setSoftDrink(softDrink);
                        ingredients.add(softDrinkIngredient);
                    }
                }
                softDrink.setIngredients(ingredients);
            }
            
            softDrinkService.updateSoftDrink(softDrink);
            logger.info("Successfully updated soft drink");
            redirectAttributes.addFlashAttribute("success", "Soft drink updated successfully");
        } catch (Exception e) {
            logger.error("Error updating soft drink", e);
            redirectAttributes.addFlashAttribute("error", "Error updating soft drink: " + e.getMessage());
        }
        return "redirect:/dashboard/soft-drinks";
    }
} 