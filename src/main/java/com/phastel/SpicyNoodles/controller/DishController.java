package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Dish;
import com.phastel.SpicyNoodles.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<Dish> createDish(@RequestBody Dish dish) {
        return ResponseEntity.ok(dishService.createDish(dish));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dish> updateDish(@PathVariable Long id, @RequestBody Dish dish) {
        dish.setId(id);
        return ResponseEntity.ok(dishService.updateDish(dish));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Long id) {
        return ResponseEntity.ok(dishService.getDishById(id));
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Dish>> searchDishesByName(@RequestParam String name) {
        return ResponseEntity.ok(dishService.searchDishesByName(name));
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Dish>> getDishesByPriceRange(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(dishService.getDishesByPriceRange(minPrice, maxPrice));
    }

    @PostMapping("/{dishId}/materials/{materialId}")
    public ResponseEntity<Dish> addMaterialToDish(
            @PathVariable Long dishId,
            @PathVariable Long materialId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(dishService.addMaterialToDish(dishId, materialId, quantity));
    }

    @DeleteMapping("/{dishId}/materials/{materialId}")
    public ResponseEntity<Dish> removeMaterialFromDish(
            @PathVariable Long dishId,
            @PathVariable Long materialId) {
        return ResponseEntity.ok(dishService.removeMaterialFromDish(dishId, materialId));
    }
} 