package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Dish;
import java.math.BigDecimal;
import java.util.List;

public interface DishService {
    Dish createDish(Dish dish);
    Dish updateDish(Dish dish);
    void deleteDish(Long id);
    Dish getDishById(Long id);
    List<Dish> getAllDishes();
    List<Dish> searchDishesByName(String name);
    List<Dish> getDishesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    Dish addIngredientToDish(Long dishId, Long ingredientId, Double quantity);
    Dish removeIngredientFromDish(Long dishId, Long ingredientId);
} 