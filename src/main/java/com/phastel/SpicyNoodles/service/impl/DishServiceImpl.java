package com.phastel.SpicyNoodles.service.impl;

import com.phastel.SpicyNoodles.entity.*;
import com.phastel.SpicyNoodles.repository.DishRepository;
import com.phastel.SpicyNoodles.repository.MaterialRepository;
import com.phastel.SpicyNoodles.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final MaterialRepository materialRepository;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository, MaterialRepository materialRepository) {
        this.dishRepository = dishRepository;
        this.materialRepository = materialRepository;
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
    public List<Dish> getDishesByPriceRange(Double minPrice, Double maxPrice) {
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
    public Dish addMaterialToDish(Long dishId, Long materialId, Integer quantity) {
        Dish dish = getDishById(dishId);
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material not found with id: " + materialId));

        DishMaterial dishMaterial = new DishMaterial();
        dishMaterial.setDish(dish);
        dishMaterial.setMaterial(material);
        dishMaterial.setQuantity(quantity);

        dish.getMaterials().add(dishMaterial);
        return dishRepository.save(dish);
    }

    @Override
    public Dish removeMaterialFromDish(Long dishId, Long materialId) {
        Dish dish = getDishById(dishId);
        dish.getMaterials().removeIf(m -> m.getMaterial().getId().equals(materialId));
        return dishRepository.save(dish);
    }
} 