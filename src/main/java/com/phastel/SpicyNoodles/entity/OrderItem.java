package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @ManyToOne
    @JoinColumn(name = "soft_drink_id")
    private SoftDrink softDrink;

    @Column(nullable = false)
    private Integer quantity;

    public String getName() {
        if (dish != null) {
            return dish.getName();
        } else if (softDrink != null) {
            return softDrink.getName();
        }
        return null;
    }
} 