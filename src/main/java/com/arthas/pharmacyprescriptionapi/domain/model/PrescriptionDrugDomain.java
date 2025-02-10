package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PrescriptionDrugSchema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDrugDomain {
    private Long id;
    private DrugDomain drug;
    private int dosage;

    public static PrescriptionDrugDomain fromSchema(PrescriptionDrugSchema schema) {
        return PrescriptionDrugDomain.builder()
                .id(schema.getId())
                .drug(DrugDomain.fromSchema(schema.getDrug()))
                .dosage(schema.getDosage())
                .build();
    }

    public PrescriptionDrugSchema toSchema() {
        return PrescriptionDrugSchema.builder()
                .id(this.id)
                .drug(this.drug.toSchema())
                .dosage(this.dosage)
                .build();
    }

    public Long getDrugId() {
        return getDrug().getId();
    }
}
