package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "soft_drinks")
public class SoftDrink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "softDrink", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SoftDrinkMaterial> materials;

    @Column(nullable = false)
    private Double price;
} 