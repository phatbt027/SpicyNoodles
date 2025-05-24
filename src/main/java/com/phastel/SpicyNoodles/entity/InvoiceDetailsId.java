package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class InvoiceDetailsId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long softDrinkId;
    private Long dishId;
} 