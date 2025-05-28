package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Storage;
import com.phastel.SpicyNoodles.entity.Ingredient;
import com.phastel.SpicyNoodles.entity.Branch;
import com.phastel.SpicyNoodles.entity.IngredientCategory;
import com.phastel.SpicyNoodles.service.StorageService;
import com.phastel.SpicyNoodles.service.IngredientService;
import com.phastel.SpicyNoodles.service.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/dashboard/storages")
public class StorageController {
    
    private static final Logger logger = LoggerFactory.getLogger(StorageController.class);
    
    private final StorageService storageService;
    private final IngredientService ingredientService;
    private final BranchService branchService;

    @Autowired
    public StorageController(StorageService storageService, IngredientService ingredientService, BranchService branchService) {
        this.storageService = storageService;
        this.ingredientService = ingredientService;
        this.branchService = branchService;
    }

    @GetMapping
    public String storageManagement(Model model) {
        logger.info("Accessing storage management page");
        model.addAttribute("storages", storageService.getAllStorages());
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        model.addAttribute("branches", branchService.getAllBranches());
        return "storage-management";
    }

    @GetMapping("/search")
    public String searchIngredients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            Model model) {
        
        logger.info("Searching ingredients with filters - name: {}, category: {}", 
            name, category);

        List<Storage> storages = storageService.getAllStorages();

        if (name != null && !name.isEmpty()) {
            storages = storages.stream()
                .filter(storage -> storage.getIngredient().getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        }

        if (category != null && !category.isEmpty()) {
            storages = storages.stream()
                .filter(storage -> storage.getIngredient().getCategory().name().equals(category))
                .toList();
        }

        model.addAttribute("storages", storages);
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        model.addAttribute("branches", branchService.getAllBranches());
        return "storage-management";
    }

    @PostMapping
    public String createStorage(@RequestParam("ingredientId") Long ingredientId,
                              @RequestParam("branchId") Long branchId,
                              @RequestParam("quantity") Integer quantity,
                              @RequestParam(value = "expirationDate", required = false) String expirationDate) {
        logger.info("Creating new storage record");
        try {
            Ingredient ingredient = ingredientService.getIngredientById(ingredientId);
            Branch branch = branchService.getBranchById(branchId);
            
            Storage storage = new Storage();
            storage.setIngredient(ingredient);
            storage.setBranch(branch);
            storage.setQuantity(quantity);
            if (expirationDate != null && !expirationDate.isEmpty()) {
                storage.setExpirationDate(LocalDateTime.parse(expirationDate));
            }
            storage.setDateOfEntry(LocalDateTime.now());
            
            storageService.createStorage(storage);
            logger.info("Successfully created storage record");
        } catch (Exception e) {
            logger.error("Error creating storage record", e);
        }
        return "redirect:/dashboard/storages";
    }

    @PostMapping("/{ingredientId}/{branchId}/delete")
    public String deleteStorage(@PathVariable Long ingredientId, @PathVariable Long branchId) {
        logger.info("Attempting to delete storage record with ingredientId: {} and branchId: {}", ingredientId, branchId);
        try {
            storageService.deleteStorage(ingredientId, branchId);
            logger.info("Successfully deleted storage record");
        } catch (Exception e) {
            logger.error("Error deleting storage record", e);
        }
        return "redirect:/dashboard/storages";
    }

    @PostMapping("/{ingredientId}/{branchId}/update")
    public String updateStorage(@PathVariable Long ingredientId, 
                              @PathVariable Long branchId, 
                              @ModelAttribute Storage storage) {
        logger.info("Attempting to update storage record with ingredientId: {} and branchId: {}", ingredientId, branchId);
        try {
            Ingredient ingredient = ingredientService.getIngredientById(ingredientId);
            Branch branch = branchService.getBranchById(branchId);
            
            storage.setIngredient(ingredient);
            storage.setBranch(branch);
            storage.setDateOfEntry(LocalDateTime.now());
            
            storageService.updateStorage(storage);
            logger.info("Successfully updated storage record");
        } catch (Exception e) {
            logger.error("Error updating storage record", e);
        }
        return "redirect:/dashboard/storages";
    }
} 