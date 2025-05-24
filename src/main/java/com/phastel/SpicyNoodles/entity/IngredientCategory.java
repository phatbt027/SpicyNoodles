package com.phastel.SpicyNoodles.entity;

public enum IngredientCategory {
    MEAT,
    VEGETABLE,
    SPICE,
    SAUCE,
    FRUIT,
    OTHER;

    public String getDisplayName() {
        return name().charAt(0) + name().substring(1).toLowerCase().replace('_', ' ');
    }
} 