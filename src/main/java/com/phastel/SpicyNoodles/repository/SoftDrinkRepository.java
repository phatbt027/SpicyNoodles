package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.SoftDrink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoftDrinkRepository extends JpaRepository<SoftDrink, Long> {
    List<SoftDrink> findByNameContainingIgnoreCase(String name);
    List<SoftDrink> findByPriceLessThanEqual(Double maxPrice);
    List<SoftDrink> findByPriceGreaterThanEqual(Double minPrice);
    List<SoftDrink> findByPriceGreaterThanEqualAndPriceLessThanEqual(Double minPrice, Double maxPrice);
} 