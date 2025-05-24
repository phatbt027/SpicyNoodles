package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByNameContainingIgnoreCase(String name);
    List<Dish> findByPriceLessThanEqual(BigDecimal maxPrice);
    List<Dish> findByPriceGreaterThanEqual(BigDecimal minPrice);
    List<Dish> findByPriceGreaterThanEqualAndPriceLessThanEqual(BigDecimal minPrice, BigDecimal maxPrice);
} 