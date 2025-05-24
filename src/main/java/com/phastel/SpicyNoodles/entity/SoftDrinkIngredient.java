package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Objects;

@Entity
@Table(name = "soft_drink_ingredients")
@Getter
@Setter
@ToString(exclude = {"softDrink", "ingredient"})
public class SoftDrinkIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soft_drink_id")
    @JsonBackReference("soft-drink-ingredients")
    private SoftDrink softDrink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    @JsonBackReference("ingredient-soft-drinks")
    private Ingredient ingredient;

    @Column(nullable = false)
    private Double quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoftDrinkIngredient that = (SoftDrinkIngredient) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 