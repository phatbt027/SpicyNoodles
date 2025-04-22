package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StorageId implements Serializable {
    private Long materialId;
    private Long branchId;

    public StorageId() {}

    public StorageId(Long materialId, Long branchId) {
        this.materialId = materialId;
        this.branchId = branchId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
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
        return Objects.equals(materialId, storageId.materialId) &&
               Objects.equals(branchId, storageId.branchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, branchId);
    }
} 