package com.phastel.SpicyNoodles.service.impl;

import com.phastel.SpicyNoodles.entity.Ingredient;
import com.phastel.SpicyNoodles.entity.IngredientCategory;
import com.phastel.SpicyNoodles.repository.IngredientRepository;
import com.phastel.SpicyNoodles.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public Ingredient updateIngredient(Ingredient ingredient) {
        if (!ingredientRepository.existsById(ingredient.getId())) {
            throw new IllegalArgumentException("Ingredient not found with id: " + ingredient.getId());
        }
        return ingredientRepository.save(ingredient);
    }

    @Override
    public void deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new IllegalArgumentException("Ingredient not found with id: " + id);
        }
        ingredientRepository.deleteById(id);
    }

    @Override
    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found with id: " + id));
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Override
    public List<Ingredient> searchIngredientsByName(String name) {
        return ingredientRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Ingredient> getIngredientsByCategory(String category) {
        try {
            IngredientCategory categoryEnum = IngredientCategory.valueOf(category);
            return ingredientRepository.findByCategory(categoryEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
    }
} 