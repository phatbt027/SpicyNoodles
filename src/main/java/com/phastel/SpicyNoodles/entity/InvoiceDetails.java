package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "invoice_details")
public class InvoiceDetails {
    @EmbeddedId
    private InvoiceDetailsId id;

    @ManyToOne
    @MapsId("softDrinkId")
    @JoinColumn(name = "soft_drink_id")
    private SoftDrink softDrink;

    @ManyToOne
    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(nullable = false)
    private Integer quantity;
}