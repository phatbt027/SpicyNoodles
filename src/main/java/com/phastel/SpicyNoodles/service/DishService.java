package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Dish;
import java.util.List;

public interface DishService {
    Dish createDish(Dish dish);
    Dish updateDish(Dish dish);
    void deleteDish(Long id);
    Dish getDishById(Long id);
    List<Dish> getAllDishes();
    List<Dish> searchDishesByName(String name);
    List<Dish> getDishesByPriceRange(Double minPrice, Double maxPrice);
    Dish addMaterialToDish(Long dishId, Long materialId, Integer quantity);
    Dish removeMaterialFromDish(Long dishId, Long materialId);
} 