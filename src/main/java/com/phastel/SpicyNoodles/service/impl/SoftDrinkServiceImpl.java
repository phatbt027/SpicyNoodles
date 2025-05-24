package com.phastel.SpicyNoodles.service.impl;

import com.phastel.SpicyNoodles.entity.*;
import com.phastel.SpicyNoodles.repository.IngredientRepository;
import com.phastel.SpicyNoodles.repository.SoftDrinkRepository;
import com.phastel.SpicyNoodles.service.SoftDrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.HashSet;

@Service
@Transactional
public class SoftDrinkServiceImpl implements SoftDrinkService {

    private final SoftDrinkRepository softDrinkRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public SoftDrinkServiceImpl(SoftDrinkRepository softDrinkRepository, IngredientRepository ingredientRepository) {
        this.softDrinkRepository = softDrinkRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public SoftDrink createSoftDrink(SoftDrink softDrink) {
        // Initialize ingredients set if null
        if (softDrink.getIngredients() == null) {
            softDrink.setIngredients(new HashSet<>());
        }
        
        // Set up bidirectional relationship for ingredients
        softDrink.getIngredients().forEach(ingredient -> {
            ingredient.setSoftDrink(softDrink);
        });
        
        return softDrinkRepository.save(softDrink);
    }

    @Override
    public SoftDrink updateSoftDrink(SoftDrink softDrink) {
        if (!softDrinkRepository.existsById(softDrink.getId())) {
            throw new IllegalArgumentException("Soft drink not found with id: " + softDrink.getId());
        }
        
        // Initialize ingredients set if null
        if (softDrink.getIngredients() == null) {
            softDrink.setIngredients(new HashSet<>());
        }
        
        // Set up bidirectional relationship for ingredients
        softDrink.getIngredients().forEach(ingredient -> {
            ingredient.setSoftDrink(softDrink);
        });
        
        return softDrinkRepository.save(softDrink);
    }

    @Override
    public void deleteSoftDrink(Long id) {
        if (!softDrinkRepository.existsById(id)) {
            throw new IllegalArgumentException("Soft drink not found with id: " + id);
        }
        softDrinkRepository.deleteById(id);
    }

    @Override
    public SoftDrink getSoftDrinkById(Long id) {
        return softDrinkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Soft drink not found with id: " + id));
    }

    @Override
    public List<SoftDrink> getAllSoftDrinks() {
        return softDrinkRepository.findAll();
    }

    @Override
    public List<SoftDrink> searchSoftDrinksByName(String name) {
        return softDrinkRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<SoftDrink> getSoftDrinksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return softDrinkRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(minPrice, maxPrice);
        } else if (minPrice != null) {
            return softDrinkRepository.findByPriceGreaterThanEqual(minPrice);
        } else if (maxPrice != null) {
            return softDrinkRepository.findByPriceLessThanEqual(maxPrice);
        }
        return getAllSoftDrinks();
    }

    @Override
    public SoftDrink addIngredientToSoftDrink(Long softDrinkId, Long ingredientId, Double quantity) {
        SoftDrink softDrink = getSoftDrinkById(softDrinkId);
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found with id: " + ingredientId));

        SoftDrinkIngredient softDrinkIngredient = new SoftDrinkIngredient();
        softDrinkIngredient.setSoftDrink(softDrink);
        softDrinkIngredient.setIngredient(ingredient);
        softDrinkIngredient.setQuantity(quantity);

        softDrink.getIngredients().add(softDrinkIngredient);
        return softDrinkRepository.save(softDrink);
    }

    @Override
    public SoftDrink removeIngredientFromSoftDrink(Long softDrinkId, Long ingredientId) {
        SoftDrink softDrink = getSoftDrinkById(softDrinkId);
        softDrink.getIngredients().removeIf(i -> i.getIngredient().getId().equals(ingredientId));
        return softDrinkRepository.save(softDrink);
    }
} 