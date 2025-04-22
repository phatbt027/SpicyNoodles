package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "storages")
public class Storage {
    @EmbeddedId
    private StorageId id;

    @ManyToOne
    @MapsId("materialId")
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne
    @MapsId("branchId")
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime dateOfEntry;

    @Column
    private LocalDateTime expirationDate;
} 