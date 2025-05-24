package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;

@Embeddable
public class StorageId implements Serializable {
    @Column(name = "ingredient_id")
    private Long ingredientId;

    @Column(name = "branch_id")
    private Long branchId;

    public StorageId() {}

    public StorageId(Long ingredientId, Long branchId) {
        this.ingredientId = ingredientId;
        this.branchId = branchId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageId storageId = (StorageId) o;
        return Objects.equals(ingredientId, storageId.ingredientId) &&
               Objects.equals(branchId, storageId.branchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, branchId);
    }
} 