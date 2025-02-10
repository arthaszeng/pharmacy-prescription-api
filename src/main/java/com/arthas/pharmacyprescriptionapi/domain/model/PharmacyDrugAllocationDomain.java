package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacyDrugAllocationSchema;
import lombok.*;

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

    public static PharmacyDrugAllocationDomain fromSchema(PharmacyDrugAllocationSchema schema, boolean includePharmacy) {
        return PharmacyDrugAllocationDomain.builder()
                .id(schema.getId())
                .pharmacy(includePharmacy ? PharmacyDomain.fromSchema(schema.getPharmacy(), false) : null)
                .drug(DrugDomain.fromSchema(schema.getDrug()))
                .allocatedStock(schema.getAllocatedStock())
                .build();
    }

    public PharmacyDrugAllocationSchema toSchema() {
        return PharmacyDrugAllocationSchema.builder()
                .id(this.id)
                .pharmacy(this.pharmacy.toSchema())
                .drug(this.drug.toSchema())
                .allocatedStock(this.allocatedStock)
                .build();
    }
}