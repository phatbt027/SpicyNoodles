package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Data
@Embeddable
public class InvoiceDetailsId implements Serializable {
    private Long softDrinkId;
    private Long dishId;
} 