package com.phastel.SpicyNoodles.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private String customerName;
    private List<OrderItemRequest> items;
    private String notes;

    @Data
    public static class OrderItemRequest {
        private Long dishId;
        private Long softDrinkId;
        private Integer quantity;
    }
} 