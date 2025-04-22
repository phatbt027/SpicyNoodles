package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "soft_drink_materials")
public class SoftDrinkMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "soft_drink_id", nullable = false)
    private SoftDrink softDrink;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(nullable = false)
    private Integer quantity;
} 