package com.arthas.pharmacyprescriptionapi.infrastructure.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharmacy_drug_allocations")
public class PharmacyDrugAllocationSchema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private PharmacySchema pharmacy;

    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private DrugSchema drug;

    @Column(nullable = false)
    private int allocatedStock;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}