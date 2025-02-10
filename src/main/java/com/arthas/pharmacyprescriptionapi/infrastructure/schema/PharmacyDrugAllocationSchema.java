package com.arthas.pharmacyprescriptionapi.infrastructure.schema;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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