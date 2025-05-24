package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.SoftDrink;
import java.math.BigDecimal;
import java.util.List;

public interface SoftDrinkService {
    SoftDrink createSoftDrink(SoftDrink softDrink);
    SoftDrink updateSoftDrink(SoftDrink softDrink);
    void deleteSoftDrink(Long id);
    SoftDrink getSoftDrinkById(Long id);
    List<SoftDrink> getAllSoftDrinks();
    List<SoftDrink> searchSoftDrinksByName(String name);
    List<SoftDrink> getSoftDrinksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    SoftDrink addIngredientToSoftDrink(Long softDrinkId, Long ingredientId, Double quantity);
    SoftDrink removeIngredientFromSoftDrink(Long softDrinkId, Long ingredientId);
} 