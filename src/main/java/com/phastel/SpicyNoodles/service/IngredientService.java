package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Ingredient;
import java.util.List;

public interface IngredientService {
    Ingredient createIngredient(Ingredient ingredient);
    Ingredient updateIngredient(Ingredient ingredient);
    void deleteIngredient(Long id);
    Ingredient getIngredientById(Long id);
    List<Ingredient> getAllIngredients();
    List<Ingredient> searchIngredientsByName(String name);
    List<Ingredient> getIngredientsByCategory(String category);
} 