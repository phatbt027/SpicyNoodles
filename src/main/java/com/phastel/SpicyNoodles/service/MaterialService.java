package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Material;
import java.util.List;

public interface MaterialService {
    Material createMaterial(Material material);
    Material updateMaterial(Material material);
    void deleteMaterial(Long id);
    Material getMaterialById(Long id);
    List<Material> getAllMaterials();
    List<Material> getMaterialsByCategory(String category);
    List<Material> searchMaterialsByName(String name);
} 