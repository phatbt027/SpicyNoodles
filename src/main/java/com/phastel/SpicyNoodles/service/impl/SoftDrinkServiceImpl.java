package com.phastel.SpicyNoodles.service.impl;

import com.phastel.SpicyNoodles.entity.*;
import com.phastel.SpicyNoodles.repository.MaterialRepository;
import com.phastel.SpicyNoodles.repository.SoftDrinkRepository;
import com.phastel.SpicyNoodles.service.SoftDrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SoftDrinkServiceImpl implements SoftDrinkService {

    private final SoftDrinkRepository softDrinkRepository;
    private final MaterialRepository materialRepository;

    @Autowired
    public SoftDrinkServiceImpl(SoftDrinkRepository softDrinkRepository, MaterialRepository materialRepository) {
        this.softDrinkRepository = softDrinkRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    public SoftDrink createSoftDrink(SoftDrink softDrink) {
        return softDrinkRepository.save(softDrink);
    }

    @Override
    public SoftDrink updateSoftDrink(SoftDrink softDrink) {
        if (!softDrinkRepository.existsById(softDrink.getId())) {
            throw new IllegalArgumentException("Soft drink not found with id: " + softDrink.getId());
        }
        return softDrinkRepository.save(softDrink);
    }

    @Override
    public void deleteSoftDrink(Long id) {
        if (!softDrinkRepository.existsById(id)) {
            throw new IllegalArgumentException("Soft drink not found with id: " + id);
        }
        softDrinkRepository.deleteById(id);
    }

    @Override
    public SoftDrink getSoftDrinkById(Long id) {
        return softDrinkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Soft drink not found with id: " + id));
    }

    @Override
    public List<SoftDrink> getAllSoftDrinks() {
        return softDrinkRepository.findAll();
    }

    @Override
    public List<SoftDrink> searchSoftDrinksByName(String name) {
        return softDrinkRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<SoftDrink> getSoftDrinksByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return softDrinkRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(minPrice, maxPrice);
        } else if (minPrice != null) {
            return softDrinkRepository.findByPriceGreaterThanEqual(minPrice);
        } else if (maxPrice != null) {
            return softDrinkRepository.findByPriceLessThanEqual(maxPrice);
        }
        return getAllSoftDrinks();
    }

    @Override
    public SoftDrink addMaterialToSoftDrink(Long softDrinkId, Long materialId, Integer quantity) {
        SoftDrink softDrink = getSoftDrinkById(softDrinkId);
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material not found with id: " + materialId));

        SoftDrinkMaterial softDrinkMaterial = new SoftDrinkMaterial();
        softDrinkMaterial.setSoftDrink(softDrink);
        softDrinkMaterial.setMaterial(material);
        softDrinkMaterial.setQuantity(quantity);

        softDrink.getMaterials().add(softDrinkMaterial);
        return softDrinkRepository.save(softDrink);
    }

    @Override
    public SoftDrink removeMaterialFromSoftDrink(Long softDrinkId, Long materialId) {
        SoftDrink softDrink = getSoftDrinkById(softDrinkId);
        softDrink.getMaterials().removeIf(m -> m.getMaterial().getId().equals(materialId));
        return softDrinkRepository.save(softDrink);
    }
} 