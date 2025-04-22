package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Material;
import com.phastel.SpicyNoodles.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping
    public ResponseEntity<Material> createMaterial(@RequestBody Material material) {
        return ResponseEntity.ok(materialService.createMaterial(material));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(@PathVariable Long id, @RequestBody Material material) {
        material.setId(id);
        return ResponseEntity.ok(materialService.updateMaterial(material));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        return ResponseEntity.ok(materialService.getAllMaterials());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Material>> getMaterialsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(materialService.getMaterialsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Material>> searchMaterialsByName(@RequestParam String name) {
        return ResponseEntity.ok(materialService.searchMaterialsByName(name));
    }
} 