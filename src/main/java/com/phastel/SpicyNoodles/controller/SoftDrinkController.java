package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.SoftDrink;
import com.phastel.SpicyNoodles.service.SoftDrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soft-drinks")
public class SoftDrinkController {

    private final SoftDrinkService softDrinkService;

    @Autowired
    public SoftDrinkController(SoftDrinkService softDrinkService) {
        this.softDrinkService = softDrinkService;
    }

    @PostMapping
    public ResponseEntity<SoftDrink> createSoftDrink(@RequestBody SoftDrink softDrink) {
        return ResponseEntity.ok(softDrinkService.createSoftDrink(softDrink));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SoftDrink> updateSoftDrink(@PathVariable Long id, @RequestBody SoftDrink softDrink) {
        softDrink.setId(id);
        return ResponseEntity.ok(softDrinkService.updateSoftDrink(softDrink));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoftDrink(@PathVariable Long id) {
        softDrinkService.deleteSoftDrink(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoftDrink> getSoftDrinkById(@PathVariable Long id) {
        return ResponseEntity.ok(softDrinkService.getSoftDrinkById(id));
    }

    @GetMapping
    public ResponseEntity<List<SoftDrink>> getAllSoftDrinks() {
        return ResponseEntity.ok(softDrinkService.getAllSoftDrinks());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SoftDrink>> searchSoftDrinksByName(@RequestParam String name) {
        return ResponseEntity.ok(softDrinkService.searchSoftDrinksByName(name));
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<SoftDrink>> getSoftDrinksByPriceRange(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(softDrinkService.getSoftDrinksByPriceRange(minPrice, maxPrice));
    }

    @PostMapping("/{softDrinkId}/materials/{materialId}")
    public ResponseEntity<SoftDrink> addMaterialToSoftDrink(
            @PathVariable Long softDrinkId,
            @PathVariable Long materialId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(softDrinkService.addMaterialToSoftDrink(softDrinkId, materialId, quantity));
    }

    @DeleteMapping("/{softDrinkId}/materials/{materialId}")
    public ResponseEntity<SoftDrink> removeMaterialFromSoftDrink(
            @PathVariable Long softDrinkId,
            @PathVariable Long materialId) {
        return ResponseEntity.ok(softDrinkService.removeMaterialFromSoftDrink(softDrinkId, materialId));
    }
} 