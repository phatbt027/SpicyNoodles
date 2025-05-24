package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "storages")
public class Storage {
    @EmbeddedId
    private StorageId id = new StorageId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("branchId")
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime dateOfEntry = LocalDateTime.now();

    @Column
    private LocalDateTime expirationDate;

    @PrePersist
    @PreUpdate
    public void updateId() {
        if (this.id == null) {
            this.id = new StorageId();
        }
        if (this.ingredient != null) {
            this.id.setIngredientId(this.ingredient.getId());
        }
        if (this.branch != null) {
            this.id.setBranchId(this.branch.getId());
        }
    }
} 