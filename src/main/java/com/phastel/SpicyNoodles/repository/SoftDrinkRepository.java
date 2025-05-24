package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.SoftDrink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SoftDrinkRepository extends JpaRepository<SoftDrink, Long> {
    List<SoftDrink> findByNameContainingIgnoreCase(String name);
    List<SoftDrink> findByPriceLessThanEqual(BigDecimal maxPrice);
    List<SoftDrink> findByPriceGreaterThanEqual(BigDecimal minPrice);
    List<SoftDrink> findByPriceGreaterThanEqualAndPriceLessThanEqual(BigDecimal minPrice, BigDecimal maxPrice);
} 