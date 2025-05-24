package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IngredientCategory category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("ingredient")
    private Set<DishIngredient> dishIngredients = new HashSet<>();

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("ingredient")
    private Set<SoftDrinkIngredient> softDrinkIngredients = new HashSet<>();
} 