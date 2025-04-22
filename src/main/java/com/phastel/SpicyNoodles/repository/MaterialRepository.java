package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByCategory(String category);
    List<Material> findByNameContainingIgnoreCase(String name);
} 