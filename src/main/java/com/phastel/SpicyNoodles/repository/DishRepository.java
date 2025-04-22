package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByNameContainingIgnoreCase(String name);
    List<Dish> findByPriceLessThanEqual(Double maxPrice);
    List<Dish> findByPriceGreaterThanEqual(Double minPrice);
    List<Dish> findByPriceGreaterThanEqualAndPriceLessThanEqual(Double minPrice, Double maxPrice);
} 