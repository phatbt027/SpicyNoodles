package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Ingredient;
import com.phastel.SpicyNoodles.entity.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByCategory(IngredientCategory category);
    List<Ingredient> findByNameContainingIgnoreCase(String name);
} 