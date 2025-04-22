package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;
} 