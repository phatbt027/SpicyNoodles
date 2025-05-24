package com.phastel.SpicyNoodles.entity;

import java.math.BigDecimal;

public interface MenuItem {
    Long getId();
    void setId(Long id);
    
    String getName();
    void setName(String name);
    
    String getDescription();
    void setDescription(String description);
    
    BigDecimal getPrice();
    void setPrice(BigDecimal price);
    
    boolean isAvailable();
    void setAvailable(boolean available);
    
    String getImageUrl();
    void setImageUrl(String imageUrl);
} 