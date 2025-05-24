package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByCategory(String category);
    List<Ingredient> findByNameContainingIgnoreCase(String name);
} 