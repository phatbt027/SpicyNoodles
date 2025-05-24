package com.phastel.SpicyNoodles.service.impl;

import com.phastel.SpicyNoodles.entity.*;
import com.phastel.SpicyNoodles.repository.DishRepository;
import com.phastel.SpicyNoodles.repository.IngredientRepository;
import com.phastel.SpicyNoodles.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Dish createDish(Dish dish) {
        return dishRepository.save(dish);
    }

    @Override
    public Dish updateDish(Dish dish) {
        if (!dishRepository.existsById(dish.getId())) {
            throw new IllegalArgumentException("Dish not found with id: " + dish.getId());
        }
        return dishRepository.save(dish);
    }

    @Override
    public void deleteDish(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new IllegalArgumentException("Dish not found with id: " + id);
        }
        dishRepository.deleteById(id);
    }

    @Override
    public Dish getDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found with id: " + id));
    }

    @Override
    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    @Override
    public List<Dish> searchDishesByName(String name) {
        return dishRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Dish> getDishesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return dishRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(minPrice, maxPrice);
        } else if (minPrice != null) {
            return dishRepository.findByPriceGreaterThanEqual(minPrice);
        } else if (maxPrice != null) {
            return dishRepository.findByPriceLessThanEqual(maxPrice);
        }
        return getAllDishes();
    }

    @Override
    public Dish addIngredientToDish(Long dishId, Long ingredientId, Double quantity) {   
        Dish dish = getDishById(dishId);
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found with id: " + ingredientId));

        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setDish(dish);
        dishIngredient.setIngredient(ingredient);
        dishIngredient.setQuantity(quantity);

        dish.getIngredients().add(dishIngredient);
        return dishRepository.save(dish);
    }

    @Override
    public Dish removeIngredientFromDish(Long dishId, Long ingredientId) {
        Dish dish = getDishById(dishId);
        dish.getIngredients().removeIf(i -> i.getIngredient().getId().equals(ingredientId));
        return dishRepository.save(dish);
    }
} 