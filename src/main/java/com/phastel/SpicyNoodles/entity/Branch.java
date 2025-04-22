package com.phastel.SpicyNoodles.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private boolean isEnabled = true;
} 