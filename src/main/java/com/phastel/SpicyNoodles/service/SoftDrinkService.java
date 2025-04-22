package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.SoftDrink;
import java.util.List;

public interface SoftDrinkService {
    SoftDrink createSoftDrink(SoftDrink softDrink);
    SoftDrink updateSoftDrink(SoftDrink softDrink);
    void deleteSoftDrink(Long id);
    SoftDrink getSoftDrinkById(Long id);
    List<SoftDrink> getAllSoftDrinks();
    List<SoftDrink> searchSoftDrinksByName(String name);
    List<SoftDrink> getSoftDrinksByPriceRange(Double minPrice, Double maxPrice);
    SoftDrink addMaterialToSoftDrink(Long softDrinkId, Long materialId, Integer quantity);
    SoftDrink removeMaterialFromSoftDrink(Long softDrinkId, Long materialId);
} 