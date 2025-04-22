package com.phastel.SpicyNoodles.service.impl;

import com.phastel.SpicyNoodles.entity.Material;
import com.phastel.SpicyNoodles.repository.MaterialRepository;
import com.phastel.SpicyNoodles.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Override
    public Material createMaterial(Material material) {
        return materialRepository.save(material);
    }

    @Override
    public Material updateMaterial(Material material) {
        if (!materialRepository.existsById(material.getId())) {
            throw new IllegalArgumentException("Material not found with id: " + material.getId());
        }
        return materialRepository.save(material);
    }

    @Override
    public void deleteMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new IllegalArgumentException("Material not found with id: " + id);
        }
        materialRepository.deleteById(id);
    }

    @Override
    public Material getMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material not found with id: " + id));
    }

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    public List<Material> getMaterialsByCategory(String category) {
        return materialRepository.findByCategory(category);
    }

    @Override
    public List<Material> searchMaterialsByName(String name) {
        return materialRepository.findByNameContainingIgnoreCase(name);
    }
} 