package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PharmacyDrugAllocationSchema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDrugAllocationDomain {
    private Long id;
    private PharmacyDomain pharmacy;
    private DrugDomain drug;
    private int allocatedStock;

    public static PharmacyDrugAllocationDomain fromSchema(PharmacyDrugAllocationSchema schema, boolean includePharmacy) {
        return new PharmacyDrugAllocationDomain(
                schema.getId(),
                includePharmacy ? PharmacyDomain.fromSchema(schema.getPharmacy(), false) : null,
                DrugDomain.fromSchema(schema.getDrug()),
                schema.getAllocatedStock()
        );
    }
}