package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "soft_drinks")
@Getter
@Setter
@ToString(exclude = "ingredients")
public class SoftDrink implements MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean available = true;

    private String imageUrl;

    @Column(nullable = false)
    private String size; // e.g., "330ml", "500ml", "1L"

    @Column(nullable = false)
    private boolean isHot = false; // for hot drinks like coffee, tea

    @OneToMany(mappedBy = "softDrink", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<SoftDrinkIngredient> ingredients = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoftDrink softDrink = (SoftDrink) o;
        return Objects.equals(id, softDrink.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 