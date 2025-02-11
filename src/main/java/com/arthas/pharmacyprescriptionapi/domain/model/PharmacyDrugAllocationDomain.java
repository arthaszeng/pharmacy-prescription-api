package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacyDrugAllocationSchema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyDrugAllocationDomain {
    private Long id;
    private PharmacyDomain pharmacy;
    private DrugDomain drug;
    private int allocatedStock;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PharmacyDrugAllocationDomain fromSchema(PharmacyDrugAllocationSchema schema, boolean includePharmacy) {
        return PharmacyDrugAllocationDomain.builder()
                .id(schema.getId())
                .pharmacy(includePharmacy ? PharmacyDomain.fromSchema(schema.getPharmacy(), false) : null)
                .drug(DrugDomain.fromSchema(schema.getDrug()))
                .allocatedStock(schema.getAllocatedStock())
                .version(schema.getVersion())
                .createdAt(schema.getCreatedAt())
                .updatedAt(schema.getUpdatedAt())
                .build();
    }

    public PharmacyDrugAllocationSchema toSchema() {
        return PharmacyDrugAllocationSchema.builder()
                .id(this.id)
                .pharmacy(this.pharmacy.toSchema())
                .drug(this.drug.toSchema())
                .allocatedStock(this.allocatedStock)
                .version(Optional.ofNullable(this.version).orElse(0L))
                .createdAt(Optional.ofNullable(this.createdAt).orElse(LocalDateTime.now()))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static boolean isActive(PharmacyDrugAllocationDomain allocationDomain) {
        return !allocationDomain.drug.isExpired() && allocationDomain.allocatedStock > 0;
    }

    public static Long getDrugId(PharmacyDrugAllocationDomain allocationDomain) {
        return allocationDomain.getDrug().getId();
    }

    public Long getPharmacyId() {
        return this.pharmacy.getId();
    }
}