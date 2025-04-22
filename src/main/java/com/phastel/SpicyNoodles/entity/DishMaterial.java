package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dish_materials")
@Data
public class DishMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(nullable = false)
    private Integer quantity;
} 